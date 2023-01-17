package de.vsy.client.controlling;

import static de.vsy.shared_transmission.packet.content.status.ClientService.MESSENGER;
import static de.vsy.shared_transmission.packet.property.communicator.CommunicationEndpoint.getClientEntity;
import static de.vsy.shared_utility.standard_value.StandardIdProvider.STANDARD_CLIENT_BROADCAST_ID;
import static de.vsy.shared_utility.standard_value.StandardIdProvider.STANDARD_CLIENT_ID;
import static java.util.Arrays.asList;

import de.vsy.client.connection_handling.ServerConnectionController;
import de.vsy.client.controlling.data_access_interfaces.AuthenticationDataModelAccess;
import de.vsy.client.controlling.data_access_interfaces.ChatDataModelAccess;
import de.vsy.client.controlling.data_access_interfaces.NotificationDataModelAccess;
import de.vsy.client.controlling.data_access_interfaces.StatusDataModelAccess;
import de.vsy.client.data_model.ServerDataCache;
import de.vsy.client.data_model.notification.SimpleInformation;
import de.vsy.client.gui.GUIController;
import de.vsy.client.packet_processing.PacketManagementUtilityProvider;
import de.vsy.client.packet_processing.PacketProcessingService;
import de.vsy.client.packet_processing.RequestPacketCreator;
import de.vsy.shared_module.data_element_validation.IdCheck;
import de.vsy.shared_module.packet_creation.PacketCompiler;
import de.vsy.shared_module.packet_management.ThreadPacketBufferManager;
import de.vsy.shared_transmission.dto.CommunicatorDTO;
import de.vsy.shared_transmission.packet.content.HumanInteractionRequest;
import de.vsy.shared_transmission.packet.content.Translatable;
import de.vsy.shared_transmission.packet.content.chat.TextMessageDTO;
import de.vsy.shared_transmission.packet.content.relation.EligibleContactEntity;
import de.vsy.shared_transmission.packet.content.status.ClientStatusChangeDTO;
import de.vsy.shared_utility.id_manipulation.IdComparator;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Base controller for all client actions. Handles basic procedures like the sequence in which the
 * initializing Packet are sent.
 */
public class ChatClientController implements AuthenticationDataModelAccess, ChatDataModelAccess,
    StatusDataModelAccess, NotificationDataModelAccess, ClientTerminator {

  private static final Logger LOGGER = LogManager.getLogger();
  private final ServerConnectionController connectionManager;
  private final ThreadPacketBufferManager packetBuffers;
  private final PacketManagementUtilityProvider packetManagement;
  private final RequestPacketCreator requester;
  private final ServerDataCache serverDataModel;
  private final GUIController guiController;
  private volatile boolean clientTerminated;
  private ExecutorService notificationProcessor;
  private ExecutorService packetProcessor;

  /**
   * Instantiates a new chat client controller.
   */
  public ChatClientController(final ServerConnectionController connectionManager,
      final PacketManagementUtilityProvider packetManagement,
      final ThreadPacketBufferManager packetBuffers,
      final RequestPacketCreator requester,
      final ServerDataCache serverDataModel,
      final GUIController guiController) {
    this.connectionManager = connectionManager;
    this.packetManagement = packetManagement;
    this.packetBuffers = packetBuffers;
    this.requester = requester;
    this.serverDataModel = serverDataModel;
    this.guiController = guiController;
    this.clientTerminated = false;
  }

  @Override
  public void closeApplication() {
    this.clientTerminated = true;
    this.connectionManager.closeConnection();
    LOGGER.info("Notification processing service shutdown initiated.");
    stopProcessor(this.notificationProcessor);
    LOGGER.info("Notification processing service terminated.");
    LOGGER.info("Packet processing service shutdown initiated.");
    stopProcessor(this.packetProcessor);
    LOGGER.info("Packet processing service terminated.");

    try {
      this.guiController.closeController();
    } catch (InterruptedException e) {
      LOGGER.error("Interrupted while waiting for GUI components to be removed.");
    }
  }

  /**
   * Adds the contact dataManagement.
   *
   * @param contactData the contact dataManagement
   * @param oldMessages the old messages
   */
  @Override
  public void addContactData(
      final EligibleContactEntity contactType,
      final CommunicatorDTO contactData,
      final List<TextMessageDTO> oldMessages) {
    int contactIndex = this.serverDataModel.addContact(contactType, contactData, oldMessages);
    this.guiController.contactStatusChange(contactIndex, contactData);
  }

  @Override
  public void removeContactData(
      final EligibleContactEntity contactType, final CommunicatorDTO contactData) {
    this.serverDataModel.removeContact(contactType, contactData);
    this.guiController.contactStatusChange(-1, contactData);
  }

  @Override
  public void completeLogin(final CommunicatorDTO clientData) {
    if (clientData != null) {
      this.serverDataModel.setCommunicatorDTO(clientData);
      this.guiController.addClientTitle(clientData);
      PacketCompiler.addOriginatorEntityProvider(
          () -> getClientEntity(clientData.getCommunicatorId()));
      sendMessengerStatus();
    } else {
      this.authenticationFailed();
    }
  }

  @Override
  public void completeLogout() {
    reset();
    PacketCompiler.addOriginatorEntityProvider(() -> getClientEntity(STANDARD_CLIENT_ID));
  }

  @Override
  public int getClientId() {
    return this.serverDataModel.getClientId();
  }

  @Override
  public boolean isClientLoggedIn() {
    final var clientId = getClientId();
    return IdCheck.checkData(clientId).isEmpty() && clientId > 0;
  }

  @Override
  public void addMessage(final TextMessageDTO message) {
    var contactId = IdComparator.determineContactId(this.serverDataModel.getClientId(),
        message.getOriginatorId(), message.getRecipientId());
    var contact = this.serverDataModel.getContactData(contactId);

    this.serverDataModel.addMessage(message);
    this.guiController.addMessage(contact, message);
  }

  @Override
  public void setupMessenger(
      final Map<Integer, List<TextMessageDTO>> messages,
      final Map<EligibleContactEntity, List<CommunicatorDTO>> activeContacts) {
    this.serverDataModel.initialMessengerSetup(messages, activeContacts);
    this.guiController.addContactsToGui();
  }

  @Override
  public void tearDownMessenger() {
    reset();
  }

  @Override
  public void completeReconnect(final boolean reconnectionSuccess) {

    if (!reconnectionSuccess) {
      this.authenticationFailed();
    } else {
      LOGGER.info("Reconnection attempt successful.");
    }
  }

  public void authenticationFailed() {
    this.serverDataModel.addNotification(
        new SimpleInformation("Authentication failed. Please reenter your credentials."));
    reset();
  }

  @Override
  public void addNotification(final Translatable notification) {
    this.serverDataModel.addNotification(notification);
  }

  @Override
  public void addRequest(final HumanInteractionRequest request) {
    this.serverDataModel.addNotification(request);
  }

  /**
   * Removes the all dataManagement.
   */
  private void reset() {
    this.serverDataModel.resetAllData();
    this.guiController.resetGUIData();
  }

  /**
   * Send messenger status.
   */
  private void sendMessengerStatus() {
    this.requester.request(
        new ClientStatusChangeDTO(MESSENGER, true, this.serverDataModel.getCommunicatorData()),
        getClientEntity(STANDARD_CLIENT_BROADCAST_ID));
  }

  private void startNewNotificationProcessor() {
    stopProcessor(this.notificationProcessor);
    final var notificationProcessingService = new NotificationProcessingService(
        this.serverDataModel, this.requester);
    this.notificationProcessor = Executors.newSingleThreadExecutor();
    this.notificationProcessor.execute(notificationProcessingService);
  }

  private void startNewPacketProcessor() {
    stopProcessor(this.packetProcessor);
    final var packetProcessingService = new PacketProcessingService(this.packetManagement, this,
        this.serverDataModel,
        this.packetBuffers, this.connectionManager);
    this.packetProcessor = Executors.newSingleThreadExecutor();
    this.packetProcessor.execute(packetProcessingService);
  }

  private void stopProcessor(ExecutorService serviceToStop) {
    if (serviceToStop != null) {
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

  public void initiateServices() {
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
  }


  public boolean clientNotTerminated() {
    return !(this.clientTerminated);
  }

  @Override
  public void resetClient() {
    reset();
  }
}
