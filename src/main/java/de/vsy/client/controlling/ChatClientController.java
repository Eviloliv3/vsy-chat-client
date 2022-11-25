/*
 *
 */
package de.vsy.client.controlling;

import static de.vsy.shared_utility.standard_value.StandardIdProvider.STANDARD_CLIENT_BROADCAST_ID;
import static de.vsy.shared_utility.standard_value.StandardIdProvider.STANDARD_SERVER_ID;
import static java.util.Collections.emptyList;

import de.vsy.client.connection_handling.ServerConnectionController;
import de.vsy.client.data_model.ClientDataManager;
import de.vsy.client.data_model.ClientNotificationManager;
import de.vsy.client.data_model.ContactDataManager;
import de.vsy.client.data_model.DataInputController;
import de.vsy.client.data_model.GUIStateManager;
import de.vsy.client.data_model.MessageManager;
import de.vsy.client.data_model.ServerDataCache;
import de.vsy.client.data_model.notification.SimpleInformation;
import de.vsy.client.gui.GUIController;
import de.vsy.client.gui.chatter_main_model.ClientChatGUI;
import de.vsy.client.packet_exception_processing.PacketHandlingExceptionProcessor;
import de.vsy.client.packet_processing.PacketProcessingService;
import de.vsy.client.packet_processing.RequestPacketCreator;
import de.vsy.client.packet_processing.processor_provisioning.PacketProcessorManager;
import de.vsy.client.packet_processing.processor_provisioning.StandardProcessorFactoryProvider;
import de.vsy.shared_module.packet_management.ThreadPacketBufferLabel;
import de.vsy.shared_module.packet_management.ThreadPacketBufferManager;
import de.vsy.shared_module.packet_validation.SemanticPacketValidator;
import de.vsy.shared_module.packet_validation.SimplePacketChecker;
import de.vsy.shared_transmission.dto.CommunicatorDTO;
import de.vsy.shared_transmission.packet.content.authentication.ReconnectRequestDTO;
import de.vsy.shared_transmission.packet.content.chat.TextMessageDTO;
import de.vsy.shared_transmission.packet.content.error.ErrorDTO;
import de.vsy.shared_transmission.packet.content.relation.EligibleContactEntity;
import de.vsy.shared_transmission.packet.content.status.ContactMessengerStatusDTO;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Base controller for all client actions. Handles basic procedures like the sequence in which the
 * initializing Packet are sent.
 */
public class ChatClientController implements ClientDataProvider, StatusMessageTriggeredActions {

  private final ServerConnectionController connectionManager;
  private final DataInputController dataController;
  private final GUIController guiController;
  private final GUIStateManager guiDataModel;
  private final Logger LOGGER = LogManager.getLogger();
  private final RequestPacketCreator requester;
  private final ServerDataCache serverDataModel;
  private final ExecutorService notificationProcessor;
  private final ExecutorService packetProcessor;
  private ThreadPacketBufferManager packetBuffers;

  /**
   * Instantiates a new chat client controller.
   *
   * @param serverAddress the server address
   * @param serverPorts   the server ports
   * @param gui           the gui
   */
  public ChatClientController(
      final String serverAddress, final int[] serverPorts, final ClientChatGUI gui) {
    setupPacketBuffers();
    this.connectionManager = new ServerConnectionController(serverAddress, serverPorts,
        this.packetBuffers);

    this.serverDataModel = new ServerDataCache(new ClientDataManager(), new ContactDataManager(),
        new MessageManager(), new ClientNotificationManager());
    this.guiDataModel = new GUIStateManager();

    //TODO nachfolgende Zuweisungen erfolgen erst nach erfolgreichem
    // Verbindungsaufbau ?
    this.requester =
        new RequestPacketCreator(
            this.packetBuffers.getPacketBuffer(ThreadPacketBufferLabel.HANDLER_BOUND));

    this.guiController =
        new GUIController(gui, this.serverDataModel, this.guiDataModel, this.requester);
    this.dataController = new DataInputController(this.serverDataModel, this);
    this.notificationProcessor = Executors.newSingleThreadExecutor();
    this.packetProcessor = Executors.newSingleThreadExecutor();
  }

  private void setupPacketBuffers() {
    this.packetBuffers = new ThreadPacketBufferManager();
    this.packetBuffers.registerPacketBuffer(ThreadPacketBufferLabel.HANDLER_BOUND);
    this.packetBuffers.registerPacketBuffer(ThreadPacketBufferLabel.OUTSIDE_BOUND);
  }

  public void closeApplication() {
    this.connectionManager.closeConnection();
    this.guiController.closeController();
    this.packetProcessor.shutdownNow();
    this.notificationProcessor.shutdownNow();

    try{
      this.notificationProcessor.awaitTermination(500, TimeUnit.MILLISECONDS);
    }catch(InterruptedException ie){
      Thread.currentThread().interrupt();
      LOGGER.error("Interrupted while waiting for NotificationProcessingService to terminate.");
    }
    try{
      this.packetProcessor.awaitTermination(100, TimeUnit.MILLISECONDS);
    }catch(InterruptedException ie){
      Thread.currentThread().interrupt();
      LOGGER.error("Interrupted while waiting for PacketProcessingService to terminate.");
    }
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
    sendMessengerStatus(true);
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
    this.serverDataModel.addNotification(new SimpleInformation("Authentication failed. Please reenter your credentials."));
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
   *
   * @param newStatus the new status
   */
  private void sendMessengerStatus(final boolean newStatus) {
    this.requester.request(
        new ContactMessengerStatusDTO(
            EligibleContactEntity.CLIENT,
            newStatus,
            this.serverDataModel.getClientAccountData().getCommunicatorDTO(),
            emptyList()),
        STANDARD_CLIENT_BROADCAST_ID);
  }

  /**
   * Gets the client dataManagement.
   *
   * @return the client dataManagement
   */
  @Override
  public CommunicatorDTO getCommunicatorDTO() {
    return this.serverDataModel.getClientAccountData().getCommunicatorDTO();
  }

  /**
   * Gets the dataManagement input controller.
   *
   * @return the dataManagement input controller
   */
  public DataInputController getDataInputController() {
    return this.dataController;
  }

  /**
   * Gets the request creator.
   *
   * @return the request creator
   */
  public RequestPacketCreator getRequestCreator() {
    return this.requester;
  }

  /**
   * Start controller.
   *
   * @throws InterruptedException the interrupted exception
   */
  public void startController() throws InterruptedException {
    final var reconnected = this.serverDataModel.getCommunicatorId() != STANDARD_SERVER_ID;

    if (connect()) {
      LOGGER.info("Connection initiated.");

      if (!reconnected) {
        final var packetProcessor = createPacketProcessingService();
        final var notificationProcessor = createNotificationProcessingService();
        this.packetProcessor.execute(packetProcessor);
        LOGGER.info("Started processing packets.");
        this.notificationProcessor.execute(notificationProcessor);
        LOGGER.info("Started processing notifications.");
      }

      if (!reconnected) {
        LOGGER.info("GUI initiated.");
        initiateGUI();
      }
      LOGGER.info("GUI started.");

      keepClientAlive();
    }
  }

  private NotificationProcessingService createNotificationProcessingService() {
    return new NotificationProcessingService(this.serverDataModel, this.requester);
  }

  private PacketProcessingService createPacketProcessingService() {
    final var packetProcessorManager = new PacketProcessorManager(this.dataController,
            new StandardProcessorFactoryProvider());
    final var processingExceptionHandler = new PacketHandlingExceptionProcessor();
    final var packetCheck = new SimplePacketChecker(new SemanticPacketValidator());

    return new PacketProcessingService(this.packetBuffers, packetProcessorManager,
        processingExceptionHandler, packetCheck, this.connectionManager);
  }

  /**
   * Connect.
   *
   * @return true, if successful
   */
  private boolean connect() {

    if (this.guiController.guiNotTerminated() && this.connectionManager.initiateConnection()) {
      tryReconnection();
      return true;
    }

    if (!this.connectionManager.getConnectionState()) {
      final var errorMessage = "You are not connected to the server (offline).";
      final var errorCause = "No connection could be initiated. Please close the application now.";
      this.serverDataModel.addNotification(new ErrorDTO(errorMessage + errorCause, null));
      removeAllData();
    }
    return false;
  }

  private void initiateGUI() {
    this.guiController.initGUIControlling();
    this.guiController.startGUI();
  }

  /**
   * Versucht Verbindungswiederaufbau, solange das GUI nicht geschlossen wurde.
   *
   * @throws InterruptedException the interrupted exception
   */
  private void keepClientAlive() throws InterruptedException {

    while (this.guiController.guiNotTerminated()) {

      if (!this.connectionManager.getConnectionState()) {
        this.connectionManager.closeConnection();
        startController();
      } else {
        try {
          Thread.sleep(1000);
        } catch (final InterruptedException ex) {
          Thread.currentThread().interrupt();
          LOGGER.info("Client can be terminated through GUI closure only.");
        }
      }
    }
    closeApplication();
  }

  private void tryReconnection() {
    CommunicatorDTO clientData = this.serverDataModel.getClientAccountData().getCommunicatorDTO();

    if (clientData != null) {
      final var clientId = clientData.getCommunicatorId();

      if (clientId != STANDARD_SERVER_ID) {
        final var reconnectRequest = new ReconnectRequestDTO(clientData);
        this.requester.request(reconnectRequest, STANDARD_CLIENT_BROADCAST_ID);
        LOGGER.info("ReconnectRequest sent.");
      }
    } else {
      LOGGER.info("Cache does not contain client data (null).");
    }
  }
}
