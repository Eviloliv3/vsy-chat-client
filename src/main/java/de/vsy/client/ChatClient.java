package de.vsy.client;

import static de.vsy.shared_module.packet_management.ThreadPacketBufferLabel.HANDLER_BOUND;
import static de.vsy.shared_module.packet_management.ThreadPacketBufferLabel.OUTSIDE_BOUND;
import static de.vsy.shared_transmission.packet.property.communicator.CommunicationEndpoint.getServerEntity;
import static de.vsy.shared_utility.standard_value.StandardIdProvider.STANDARD_CLIENT_ID;
import static de.vsy.shared_utility.standard_value.StandardIdProvider.STANDARD_SERVER_ID;
import static de.vsy.shared_utility.standard_value.ThreadContextValues.STANDARD_CLIENT_ROUTE_VALUE;

import de.vsy.client.connection_handling.ClientConnectionWatcher;
import de.vsy.client.connection_handling.ServerConnectionController;
import de.vsy.client.controlling.ChatClientController;
import de.vsy.client.data_model.ClientDataManager;
import de.vsy.client.data_model.ClientNotificationManager;
import de.vsy.client.data_model.ContactDataManager;
import de.vsy.client.data_model.MessageManager;
import de.vsy.client.data_model.ServerDataCache;
import de.vsy.shared_transmission.packet.content.notification.SimpleInformationDTO;
import de.vsy.client.gui.ClientChatGUI;
import de.vsy.client.gui.GUIController;
import de.vsy.client.gui.GUIInteractionProcessor;
import de.vsy.client.packet_processing.PacketManagementUtilityProvider;
import de.vsy.client.packet_processing.RequestPacketCreator;
import de.vsy.shared_module.packet_creation.ContentIdentificationProviderImpl;
import de.vsy.shared_module.packet_creation.PacketCompiler;
import de.vsy.shared_module.packet_management.ThreadPacketBufferManager;
import de.vsy.shared_transmission.dto.CommunicatorDTO;
import de.vsy.shared_transmission.packet.content.authentication.ReconnectRequestDTO;
import de.vsy.shared_transmission.packet.property.communicator.CommunicationEndpoint;
import de.vsy.shared_utility.standard_value.ThreadContextValues;
import java.time.Instant;
import java.util.Arrays;
import java.util.Timer;
import java.util.concurrent.CountDownLatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

/**
 * Handles Client Connection. Processes client requests and transfers requests into server readable
 * requests.
 */
public class ChatClient {

  private static final int[] SERVER_PORTS = {7370, 7371};
  private static final String SERVER_ADDRESS = "127.0.0.1";
  private static final Logger LOGGER = LogManager.getLogger();
  private final ServerConnectionController connectionManager;
  private final ServerDataCache serverDataModel;
  private final RequestPacketCreator requester;
  private GUIController guiController;
  private ThreadPacketBufferManager packetBuffers;
  private ChatClientController clientController;
  private Timer connectionWatcher;
  private CountDownLatch connectionWaiter;

  public ChatClient() {
    setupPacketBuffers();
    this.connectionManager = new ServerConnectionController(SERVER_ADDRESS, SERVER_PORTS,
        this.packetBuffers);
    this.serverDataModel = new ServerDataCache(new ClientDataManager(), new ContactDataManager(),
        new MessageManager(), new ClientNotificationManager());
    var packetManagement = new PacketManagementUtilityProvider();
    this.requester = new RequestPacketCreator(this.packetBuffers.getPacketBuffer(HANDLER_BOUND),
        packetManagement.getPacketTransmissionCache(),
        packetManagement.getResultingPacketContentHandler());
    this.clientController = new ChatClientController(this.connectionManager, packetManagement,
        this.packetBuffers, this.requester, this.serverDataModel);
    setupGUIController();
  }

  /**
   * The main method.
   *
   * @param args the arguments
   */
  public static void main(final String[] args) {
    ThreadContext.put(ThreadContextValues.LOG_ROUTE_CONTEXT_KEY, STANDARD_CLIENT_ROUTE_VALUE);
    ThreadContext.put(ThreadContextValues.LOG_FILE_CONTEXT_KEY, "ChatClient[" + Instant.now() + "]");
    final var client = new ChatClient();
    setupPacketCompiler();
    client.handleClient();
  }

  private static void setupPacketCompiler() {
    PacketCompiler.addOriginatorEntityProvider(
        () -> CommunicationEndpoint.getClientEntity(STANDARD_CLIENT_ID));
    PacketCompiler.addContentIdentificationProvider(new ContentIdentificationProviderImpl());
  }

  private void setupPacketBuffers() {
    this.packetBuffers = new ThreadPacketBufferManager();
    this.packetBuffers.registerPacketBuffer(HANDLER_BOUND);
    this.packetBuffers.registerPacketBuffer(OUTSIDE_BOUND);
  }

  private void setupGUIController() {
    ClientChatGUI gui = new ClientChatGUI();
    GUIInteractionProcessor guiInteractions = new GUIInteractionProcessor(gui, gui,
        this.clientController,
        this.serverDataModel, this.requester);
    this.guiController = new GUIController(gui, this.serverDataModel, guiInteractions);
    this.clientController.setGUIController(this.guiController);
  }

  private boolean connect() throws InterruptedException {
    final var connectionEstablished = this.connectionManager.initiateConnection();

    if (connectionEstablished) {
      setupConnectionWatcher();

      if (this.serverDataModel.getClientId() != STANDARD_CLIENT_ID) {
        tryReconnection();
      } else {
        LOGGER.trace("Reconnection not attempted, GUI not ready.");
      }
    } else {
      final var errorCause = "No connection could be initiated. If you do not want to attempt another reconnection try close the application within 10 seconds.";
      final var notification = new SimpleInformationDTO(errorCause);
      this.serverDataModel.addNotification(notification);
      this.clientController.resetClient();
      Thread.sleep(10000);
    }
    return connectionEstablished;
  }

  private void setupConnectionWatcher() {
    if (this.connectionWatcher != null) {
      this.connectionWatcher.cancel();
    }
    this.connectionWatcher = new Timer("ClientConnectionWatcher");
    this.connectionWaiter = new CountDownLatch(1);
    var connectionWatcherTask = new ClientConnectionWatcher(this.connectionManager,
        this.connectionWaiter);
    this.connectionWatcher.scheduleAtFixedRate(connectionWatcherTask, 50, 1000);
  }

  private void tryReconnection() throws InterruptedException {
    CommunicatorDTO clientData = this.serverDataModel.getCommunicatorData();

    if (clientData != null) {
      final var clientId = clientData.getCommunicatorId();

      if (clientId != STANDARD_CLIENT_ID) {
        final var reconnectRequest = new ReconnectRequestDTO(clientData);
        Thread.sleep(1000);
        this.requester.request(reconnectRequest, getServerEntity(STANDARD_SERVER_ID));
        LOGGER.info("ReconnectRequest sent.");
      } else {
        LOGGER.error("No Client data found.");
      }
    } else {
      LOGGER.info("Cache does not contain client data (null).");
    }
  }

  public void handleClient() {
    LOGGER.info("Client started.");

    while (this.clientController.clientNotTerminated()) {

      try {

        if (connect()) {
          LOGGER.info("Connection initiated.");
          this.clientController.initiateServices();
          initiateGUI();
          this.connectionWaiter.await();
          resetConnection();
        } else {
          this.clientController.resetClient();
          LOGGER.error("No connection could be established.");
        }
      } catch (InterruptedException ie) {
        LOGGER.error("Interrupted while: {}", Arrays.asList(ie.getStackTrace()));
        this.clientController.closeApplication();
      }
    }
  }

  private void initiateGUI() {
    if (!this.guiController.guiNotTerminated()) {
      this.guiController.startGUI();
      this.guiController.startInteracting();
      LOGGER.info("GUI initiated.");
    } else {
      LOGGER.trace("GUI is still active.");
    }
  }

  private void resetConnection() {
    this.connectionWatcher.cancel();
    this.connectionWatcher.purge();
    this.connectionManager.closeConnection();
  }
}
