/*
 *
 */
package de.vsy.client.controlling;

import static de.vsy.shared_module.packet_management.ThreadPacketBufferLabel.HANDLER_BOUND;
import static de.vsy.shared_transmission.packet.content.status.ClientService.MESSENGER;
import static de.vsy.shared_transmission.packet.property.communicator.CommunicationEndpoint.getClientEntity;
import static de.vsy.shared_transmission.packet.property.communicator.CommunicationEndpoint.getServerEntity;
import static de.vsy.shared_utility.standard_value.StandardIdProvider.STANDARD_CLIENT_BROADCAST_ID;
import static de.vsy.shared_utility.standard_value.StandardIdProvider.STANDARD_SERVER_ID;
import static java.util.Arrays.asList;

import de.vsy.client.connection_handling.ClientConnectionWatcher;
import de.vsy.client.connection_handling.ServerConnectionController;
import de.vsy.client.data_model.ClientDataManager;
import de.vsy.client.data_model.ClientNotificationManager;
import de.vsy.client.data_model.ContactDataManager;
import de.vsy.client.data_model.GUIStateManager;
import de.vsy.client.data_model.InputController;
import de.vsy.client.data_model.MessageManager;
import de.vsy.client.data_model.ServerDataCache;
import de.vsy.client.data_model.notification.SimpleInformation;
import de.vsy.client.gui.GUIController;
import de.vsy.client.gui.chatter_main_model.ClientChatGUI;
import de.vsy.client.gui.chatter_main_model.GUIInteractionProcessor;
import de.vsy.client.packet_processing.PacketManagementUtilityProvider;
import de.vsy.client.packet_processing.PacketProcessingService;
import de.vsy.client.packet_processing.RequestPacketCreator;
import de.vsy.shared_module.packet_management.ThreadPacketBufferLabel;
import de.vsy.shared_module.packet_management.ThreadPacketBufferManager;
import de.vsy.shared_transmission.dto.CommunicatorDTO;
import de.vsy.shared_transmission.packet.content.authentication.ReconnectRequestDTO;
import de.vsy.shared_transmission.packet.content.chat.TextMessageDTO;
import de.vsy.shared_transmission.packet.content.status.ClientStatusChangeDTO;
import java.util.Arrays;
import java.util.Timer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Base controller for all client actions. Handles basic procedures like the sequence in which the
 * initializing Packet are sent.
 */
public class ChatClientController implements StatusMessageTriggeredActions {

  private final ServerConnectionController connectionManager;
  private final InputController dataController;
  private final PacketManagementUtilityProvider packetManagement;
  private final GUIController guiController;
  private final GUIStateManager guiDataModel;
  private final Logger LOGGER = LogManager.getLogger();
  private final RequestPacketCreator requester;
  private final ServerDataCache serverDataModel;
  private ExecutorService notificationProcessor;
  private ExecutorService packetProcessor;
  private Timer connectionWatcher;
  private CountDownLatch connectionWaiter;
  private ThreadPacketBufferManager packetBuffers;

  /**
   * Instantiates a new chat client controller.
   *
   * @param serverAddress the server address
   * @param serverPorts   the server ports
   */
  public ChatClientController(final String serverAddress, final int[] serverPorts) {
    setupPacketBuffers();
    this.connectionManager = new ServerConnectionController(serverAddress, serverPorts,
        this.packetBuffers);
    this.serverDataModel = new ServerDataCache(new ClientDataManager(), new ContactDataManager(),
        new MessageManager(), new ClientNotificationManager());
    this.guiDataModel = new GUIStateManager();
    this.packetManagement = new PacketManagementUtilityProvider();
    this.requester = new RequestPacketCreator(this.packetBuffers.getPacketBuffer(HANDLER_BOUND), this.packetManagement.getPacketTransmissionCache(),
            this.packetManagement.getResultingPacketContentHandler());
    this.dataController = new InputController(this.serverDataModel, this);
    this.guiController = setupGUIController();
  }

  private void setupPacketBuffers() {
    this.packetBuffers = new ThreadPacketBufferManager();
    this.packetBuffers.registerPacketBuffer(HANDLER_BOUND);
    this.packetBuffers.registerPacketBuffer(ThreadPacketBufferLabel.OUTSIDE_BOUND);
  }

  private GUIController setupGUIController(){
    ClientChatGUI gui = new ClientChatGUI();
    GUIInteractionProcessor guiInteractions = new GUIInteractionProcessor(gui, this.serverDataModel, this.guiDataModel, this.requester);
    return new GUIController(gui, this.serverDataModel, this.guiDataModel, guiInteractions);
  }

  public void closeApplication() {
    this.connectionManager.closeConnection();
    this.guiController.closeController();
    stopProcessor(this.notificationProcessor);
    stopProcessor(this.packetProcessor);
    removeAllData();
  }

  @Override
  public void changeContactStatus(final CommunicatorDTO contact, final boolean status) {
    this.guiController.contactStatusChange(contact, status);
  }

  @Override
  public void completeLogin() {
    this.guiController.processClientData();
    this.guiController.addContactsToGui();
    sendMessengerStatus();
  }

  @Override
  public void completeLogout() {
    this.guiController.resetGUIData();
  }

  @Override
  public void messageReceived(final TextMessageDTO message) {
    final var contactId = this.guiDataModel.getActiveChatContact().getCommunicatorId();

    if (contactId == message.getOriginatorId() || contactId == message.getRecipientId()) {
      this.guiDataModel.addMessage(message);
    }
  }

  @Override
  public void completeMessengerSetup() {
    this.guiController.addContactsToGui();
  }

  @Override
  public void completeMessengerTearDown() {
    this.guiController.addContactsToGui();
    this.guiDataModel.clearActiveChat();
  }

  @Override
  public void reconnectFailed() {
    this.serverDataModel.addNotification(
        new SimpleInformation("Authentication failed. Please reenter your credentials."));
    removeAllData();
  }

  /**
   * Removes the all dataManagement.
   */
  private void removeAllData() {
    this.serverDataModel.resetAllData();
    this.guiDataModel.resetAllData();
  }

  /**
   * Send messenger status.
   */
  private void sendMessengerStatus() {
    this.requester.request(
        new ClientStatusChangeDTO(MESSENGER, true, this.serverDataModel.getCommunicatorData()),
        getClientEntity(STANDARD_CLIENT_BROADCAST_ID));
  }

  private void startNewNotificationProcessor(){
    stopProcessor(this.notificationProcessor);
    final var notificationProcessingService = new NotificationProcessingService(this.serverDataModel, this.requester);
    this.notificationProcessor = Executors.newSingleThreadExecutor();
    this.notificationProcessor.execute(notificationProcessingService);
  }

  private void startNewPacketProcessor(){
    stopProcessor(this.packetProcessor);
    final var packetProcessingService = new PacketProcessingService(this.packetManagement, this.dataController, this.serverDataModel,
        this.packetBuffers,  this.connectionManager);
    this.packetProcessor = Executors.newSingleThreadExecutor();
    this.packetProcessor.execute(packetProcessingService);
  }

  private void stopProcessor(ExecutorService serviceToStop){
    if(serviceToStop != null) {
      try {
        final var threadList = serviceToStop.shutdownNow();
        var serviceStopped = serviceToStop.awaitTermination(1000, TimeUnit.MILLISECONDS);
        if (serviceStopped) {
          for (final var currentThread : threadList) {
            LOGGER.info("{} instance stopped.", currentThread.getClass().getSimpleName());
          }
        } else {
          final var errorMessage = "Thread instances could not be stopped: " + Arrays.toString(
              threadList.toArray());
          throw new RuntimeException(errorMessage);
        }
      } catch (InterruptedException ie) {
        LOGGER.error("Interrupted while waiting for service termination. {}",
            asList(ie.getStackTrace()));
      }
    }
  }

  /**
   * Start controller.
   *
   * @throws InterruptedException the interrupted exception
   */
  public void startController() throws InterruptedException {

    if (connect()) {
      LOGGER.info("Connection initiated.");

      if (this.packetProcessor == null || this.packetProcessor.isShutdown()) {
        startNewPacketProcessor();
        LOGGER.info("PacketProcessingService started.");
      } else {
        LOGGER.trace("PacketProcessingService is still working.");
      }

      if (this.notificationProcessor == null || this.notificationProcessor.isShutdown()) {
        startNewNotificationProcessor();
        LOGGER.info("NotificationProcessor started.");
      } else {
        LOGGER.trace("NotificationProcessor is still working.");
      }

      if (!this.guiController.guiNotTerminated()) {
        initiateGUI();
        LOGGER.info("GUI initiated.");
      } else {
        LOGGER.trace("GUI is still active.");
      }
      keepClientAlive();
    } else {
      LOGGER.error("No connection could be established.");
    }
  }

  /**
   * Connect.
   *
   * @return true, if successful
   */
  private boolean connect() {
    final var connectionEstablished = this.connectionManager.initiateConnection();

    if (connectionEstablished) {
      setupConnectionWatcher();

      if (this.guiController.guiNotTerminated()) {
        tryReconnection();
      } else {
        LOGGER.trace("Reconnection not attempted, GUI not ready.");
      }
    } else {
      final var errorCause = "No connection could be initiated. If you do not want to attempt another reconnection try close the application within 10 seconds.";
      final var notification = new SimpleInformation(errorCause);
      this.serverDataModel.addNotification(notification);
      //TODO should the application shutdown automatically at this point?
      //this.closeApplication();
      try {
        Thread.sleep(10000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      removeAllData();
    }
    return connectionEstablished;
  }

  private void setupConnectionWatcher(){
    if(this.connectionWatcher != null) {
      this.connectionWatcher.cancel();
    }
    this.connectionWatcher = new Timer("ClientConnectionWatcher");
    this.connectionWaiter = new CountDownLatch(1);
    var connectionWatcherTask = new ClientConnectionWatcher(this.connectionManager, this.connectionWaiter);
    this.connectionWatcher.scheduleAtFixedRate(connectionWatcherTask, 50, 1000);
  }

  private void initiateGUI() {
    this.guiController.initGUIControlling();
    this.guiController.startGUI();
  }

  /**
   * Tries to reconnect to server, until the GUI is closed.
   *
   * @throws InterruptedException the interrupted exception
   */
  private void keepClientAlive() throws InterruptedException {

    while (this.guiController.guiNotTerminated()) {
      this.connectionWaiter.await();
      this.connectionWatcher.cancel();
      this.connectionWatcher.purge();
      this.connectionManager.closeConnection();
      startController();
    }
    closeApplication();
  }

  private void tryReconnection() {
    CommunicatorDTO clientData = this.serverDataModel.getCommunicatorData();

    if (clientData != null) {
      final var clientId = clientData.getCommunicatorId();

      if (clientId != STANDARD_SERVER_ID) {
        final var reconnectRequest = new ReconnectRequestDTO(clientData);
        this.requester.request(reconnectRequest, getServerEntity(STANDARD_SERVER_ID));
        LOGGER.info("ReconnectRequest sent.");
      }
    } else {
      LOGGER.info("Cache does not contain client data (null).");
    }
  }
}
