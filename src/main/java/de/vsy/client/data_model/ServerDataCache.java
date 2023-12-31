package de.vsy.client.data_model;

import static de.vsy.shared_utility.standard_value.StandardIdProvider.STANDARD_CLIENT_ID;

import de.vsy.shared_module.packet_management.ClientDataProvider;
import de.vsy.shared_transmission.dto.CommunicatorDTO;
import de.vsy.shared_transmission.packet.content.Translatable;
import de.vsy.shared_transmission.packet.content.chat.TextMessageDTO;
import de.vsy.shared_transmission.packet.content.relation.EligibleContactEntity;
import de.vsy.shared_utility.id_manipulation.IdComparator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Manages all dataManagement that originated from server response Packet.
 */
//TODO Can store contact data for the purpose of resolving contact names from contact ids or vice versa.
public class ServerDataCache implements ClientDataProvider {

  private static final Logger LOGGER = LogManager.getLogger();
  private final ContactDataManager activeContactController;
  private final MessageManager messageController;
  private final ClientNotificationManager notificationManager;
  private ClientDataManager clientData;

  public ServerDataCache(final ClientDataManager clientDataManager,
      final ContactDataManager activeContactManager, final MessageManager messageManager,
      final ClientNotificationManager notificationManager) {
    this.clientData = clientDataManager;
    this.activeContactController = activeContactManager;
    this.messageController = messageManager;
    this.notificationManager = notificationManager;
  }

  /**
   * Adds the active contacts.
   *
   * @param activeContacts the active contacts
   */
  public void addActiveContacts(
      final Map<EligibleContactEntity, List<CommunicatorDTO>> activeContacts) {
    this.activeContactController.setNewClientList(activeContacts);
  }

  /**
   * Adds the active contact.
   *
   * @param newClient the new client
   * @param messages  the messages
   */
  public int addContact(
      final EligibleContactEntity contactType,
      final CommunicatorDTO newClient,
      final List<TextMessageDTO> messages) {
    int contactIndex = -1;

    if (contactType == null || newClient == null) {
      LOGGER.error("Contact type ({}) or contact data ({}) invalid.", contactType, newClient);
      return contactIndex;
    }
    final var messageHistory = messages != null ? messages : new ArrayList<TextMessageDTO>();
    contactIndex = this.activeContactController.addContact(contactType, newClient);
    this.messageController.setMessagesForClient(newClient.getCommunicatorId(), messageHistory);
    return contactIndex;
  }

  public CommunicatorDTO getContactData(final int contactId) {
    return this.activeContactController.getContactData(contactId);
  }

  /**
   * Adds the dialog message.
   *
   * @param notification the notification
   */
  public void addNotification(final Translatable notification) {
    this.notificationManager.addNotification(notification);
  }

  public void addPriorityNotification(final Translatable notification) {
    this.notificationManager.prependNotification(notification);
  }

  public ClientNotificationManager getClientNotificationManager() {
    return this.notificationManager;
  }

  /**
   * Adds the message.
   *
   * @param msg the msg
   */
  public void addMessage(final TextMessageDTO msg) {
    final var clientId = this.clientData.getCommunicatorDTO().getCommunicatorId();
    final var contactId = IdComparator.determineContactId(clientId, msg.getOriginatorId(),
        msg.getRecipientId());
    this.messageController.addMessage(contactId, msg);
  }

  /**
   * Gets the client account dataManagement.
   *
   * @return the client account dataManagement
   */
  public ClientDataManager getClientAccountData() {
    return this.clientData;
  }


  /**
   * Gets the client id.
   *
   * @return the client id
   */
  @Override
  public int getClientId() {
    int clientId = STANDARD_CLIENT_ID;

    if (this.clientData != null) {
      final var clientInfo = this.clientData.getCommunicatorDTO();

      if (clientInfo != null) {
        clientId = clientInfo.getCommunicatorId();
      }
    }
    return clientId;
  }

  @Override
  public CommunicatorDTO getCommunicatorData() {
    return this.clientData.getCommunicatorDTO();
  }

  /**
   * Gets the contact list.
   *
   * @return the contact list
   */
  public List<CommunicatorDTO> getContactList() {
    return this.activeContactController.getActiveContactList();
  }

  /**
   * Gets the message list.
   *
   * @param clientId the client id
   * @return the message list
   */
  public List<TextMessageDTO> getMessageList(final int clientId) {
    return this.messageController.getMessages(clientId);
  }

  /**
   * Initial messenger setup.
   *
   * @param newMsgMap      the new msg map
   * @param activeContacts the active clients
   */
  public void initialMessengerSetup(
      final Map<Integer, List<TextMessageDTO>> newMsgMap,
      final Map<EligibleContactEntity, List<CommunicatorDTO>> activeContacts) {
    this.activeContactController.setNewClientList(activeContacts);
    this.messageController.setNewMessageMap(newMsgMap);
  }

  /**
   * Removes the client.
   *
   * @param contactType the client id
   * @param contactData the contact dataManagement
   */
  public void removeContact(
      final EligibleContactEntity contactType, final CommunicatorDTO contactData) {
    this.activeContactController.removeContact(contactType, contactData);
    this.messageController.removeMessagesForClient(contactData.getCommunicatorId());
  }

  public void resetAllData() {
    this.clientData = new ClientDataManager();
    this.activeContactController.resetContactList();
    this.messageController.setNewMessageMap(new HashMap<>());
  }

  /**
   * Lists the client dataManagement.
   *
   * @param client the new client dataManagement
   */
  public void setCommunicatorDTO(final CommunicatorDTO client) {
    this.clientData.setCommunicatorDTO(client);
  }
}
