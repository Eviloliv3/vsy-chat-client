/*
 *
 */
package de.vsy.chat.client.controlling;

import de.vsy.chat.client.connection_handling.ConnectionManager;
import de.vsy.chat.client.controlling.packet_creation.RequestPacketCreator;
import de.vsy.chat.client.data_model.DataInputController;
import de.vsy.chat.client.data_model.GUIStateDataManager;
import de.vsy.chat.client.data_model.ServerProvidedDataManager;
import de.vsy.chat.client.gui.GUIController;
import de.vsy.chat.client.gui.chatter_main_model.ClientChatGUI;
import de.vsy.chat.client.packet_exception_processing.PacketHandlingExceptionProcessor;
import de.vsy.chat.client.packet_processing.PacketProcessingService;
import de.vsy.chat.client.packet_processing.processor_provisioning.PacketProcessorProvider;
import de.vsy.chat.client.packet_processing.processor_provisioning.StandardCategoryProcessorFactoryProvider;
import de.vsy.chat.module.packet_management.ThreadPacketBufferLabel;
import de.vsy.chat.module.packet_management.ThreadPacketBufferManager;
import de.vsy.chat.module.packet_validation.SimplePacketChecker;
import de.vsy.chat.module.packet_validation.content_validation.ClientPacketTypeValidationCreator;
import de.vsy.chat.transmission.dto.CommunicatorDTO;
import de.vsy.chat.transmission.packet.content.authentication.ReconnectRequestDTO;
import de.vsy.chat.transmission.packet.content.chat.TextMessageDTO;
import de.vsy.chat.transmission.packet.content.error.ErrorDTO;
import de.vsy.chat.transmission.packet.content.relation.EligibleContactEntity;
import de.vsy.chat.transmission.packet.content.status.ContactMessengerStatusDTO;

import static de.vsy.chat.utility.standard_value.StandardIdProvider.STANDARD_CLIENT_MULTICAST_ID;
import static de.vsy.chat.utility.standard_value.StandardIdProvider.STANDARD_SERVER_ID;
import static java.util.Collections.emptyList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Base controller for all client actions. Handles basic procedures like the packet sequence in
 * which the initializing Packet are sent.
 */
public class ChatClientController implements ClientDataProvider, StatusMessageTriggeredActions {

  private final ConnectionManager connectionManager;
  private final DataInputController dataController;
  private final GUIController guiController;
  private final GUIStateDataManager guiDataModel;
  private final Logger logger;
  private final RequestPacketCreator requester;
  private final ServerProvidedDataManager serverDataModel;
  private ThreadPacketBufferManager packetBuffers;

  /**
   * Instantiates a new chat client controller.
   *
   * @param serverAddress the server address
   * @param serverPorts the server ports
   * @param gui the gui
   */
  public ChatClientController(
      final String serverAddress, final int[] serverPorts, final ClientChatGUI gui) {
    setupPacketBuffers();
    logger = LogManager.getLogger();
    this.connectionManager = new ConnectionManager(serverAddress, serverPorts, this.packetBuffers);

    this.serverDataModel = new ServerProvidedDataManager();
    this.guiDataModel = new GUIStateDataManager();

    // TODO nachfolgende Zuweisungen erfolgen erst nach erfolgreichem
    // Verbindungsaufbau ?
    this.requester =
        new RequestPacketCreator(
            this.packetBuffers.getPacketBuffer(ThreadPacketBufferLabel.HANDLER_BOUND));

    this.guiController =
        new GUIController(gui, this.serverDataModel, this.guiDataModel, this.requester);
    this.dataController = new DataInputController(this.serverDataModel, this);
    this.serverDataModel.setDialogRequestProcessor(this.guiController);
    var p =
        new PacketProcessingService(
            this.packetBuffers,
            new PacketProcessorProvider(
                this.dataController, new StandardCategoryProcessorFactoryProvider()),
            new PacketHandlingExceptionProcessor(),
            new SimplePacketChecker(
                ClientPacketTypeValidationCreator.createRegularPacketContentValidator()),
            connectionManager);
  }

  private void setupPacketBuffers() {
    this.packetBuffers = new ThreadPacketBufferManager();
    this.packetBuffers.registerPackerBuffer(ThreadPacketBufferLabel.HANDLER_BOUND);
    this.packetBuffers.registerPackerBuffer(ThreadPacketBufferLabel.OUTSIDE_BOUND);
  }

  public void closeApplication() {
    this.connectionManager.closeConnection();
    this.guiController.closeController();

    try {
      this.dataController.closeController();
    } catch (final InterruptedException e) {
      Thread.currentThread().interrupt();
      this.logger.info("Warten auf PacketProcessingService unterbrochen.");
    }

    removeAllData();
  }

  // -------------------------------------------
  // ---Client-Data-Provider--------------------
  @Override
  public void contactOnlineStatusChange(final CommunicatorDTO contact, final boolean status) {
    this.guiController.contactStatusChange(contact, status);
  }

  // -------------------------------------------
  @Override
  public void loginComplete() {
    this.guiController.processClientData();
    this.guiController.addContactsToGui();
    sendMessengerStatus(true);
  }

  @Override
  public void logoutComplete() {
    this.guiController.resetGUIData();
  }

  @Override
  public void messageReceived(final TextMessageDTO message) {
    final var contactId = this.guiDataModel.getActiveChatContact().getCommunicatorId();

    if (contactId == message.getOriginatorId() || contactId == message.getRecipientId()) {
      this.guiDataModel.addMessage(message);
    }
  }

  // -------------------------------------------
  // ---Server-Input-Triggered-Actions----------
  @Override
  public void messengerSetupComplete() {
    this.guiController.addContactsToGui();
  }

  @Override
  public void messengerTearDownComplete() {
    this.guiController.addContactsToGui();
    this.guiDataModel.clearActiveChat();
  }

  @Override
  public void reconnectFailed() {
    this.serverDataModel.addDialogMessage(
        "Sie konnten nicht automatisch authentifiziert werden. Bitte melden Sie sich erneut an.");
    removeAllData();
  }

  /** Removes the all dataManagement. */
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
        STANDARD_CLIENT_MULTICAST_ID);
  }

  /**
   * Gets the client dataManagement.
   *
   * @return the client dataManagement
   */
  // -------------------------------------------
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
   * Gets the GUI controller.
   *
   * @return the GUI controller
   */
  public DialogRequestProcessor getGUIController() {
    return this.guiController;
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
      this.logger.info("Verbindung aufgebaut.");

      if (!reconnected) {
        this.logger.info("Inputverarbeitung wird gestartet.");
        this.dataController.startPacketProcessing();
      }
      this.logger.info("Input wird verarbeitet.");

      if (!reconnected) {
        this.logger.info("GUI wird gestartet.");
        initiateGUI();
      }
      this.logger.info("GUI gestartet.");

      keepClientAlive();
    }
  }

  /**
   * Connect.
   *
   * @return true, if successful
   */
  private boolean connect() {

    if (!this.guiController.guiTerminationState() && this.connectionManager.initiateConnection()) {
      tryReconnection();
      return true;
    }

    if (!this.connectionManager.getConnectionState()) {
      final var errorMessage = "Sie sind nicht mit dem Server verbunden (offline).";
      final var errorCause =
          "Alle Verbindungsversuche sind gescheitert. Bitte beenden Sie die Applikation.";
      this.serverDataModel.addDialogMessage(new ErrorDTO(errorMessage + errorCause, null));
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

    while (!this.guiController.guiTerminationState()) {

      if (!this.connectionManager.getConnectionState()) {
        this.connectionManager.closeConnection();

        startController();
      } else {

        try {
          Thread.sleep(1000);
        } catch (final InterruptedException ex) {
          Thread.currentThread().interrupt();
          this.logger.info("Klient kann nur durch GUI-Schliessung beendet werden.");
        }
      }
    }
    closeApplication();
  }

  private void tryReconnection() {
    CommunicatorDTO clientData;

    if ((clientData = this.serverDataModel.getClientAccountData().getCommunicatorDTO()) != null) {
      final var clientId = clientData.getCommunicatorId();

      if (clientId != STANDARD_SERVER_ID) {
        this.requester.request(new ReconnectRequestDTO(clientData), STANDARD_CLIENT_MULTICAST_ID);
        this.logger.info("ReconnectRequest gesendet.");
      }
    } else {
      this.logger.info("Der Zwischenspeicher enthaelt keine Clientdaten (null).");
    }
  }
}
