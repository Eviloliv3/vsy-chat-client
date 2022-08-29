package de.vsy.chat.client.data_model;

import de.vsy.chat.client.controlling.DialogRequestProcessor;
import de.vsy.chat.client.data_model.sub_unit.ContactDataController;
import de.vsy.chat.client.data_model.sub_unit.MessageController;
import de.vsy.chat.client.gui.essential_graphical_units.dialog.DialogType;
import de.vsy.chat.module.packet_content_translation.HumanInteractionRequestTranslator;

import static de.vsy.chat.client.gui.essential_graphical_units.dialog.DialogType.INFORMATION;
import static de.vsy.chat.client.gui.essential_graphical_units.dialog.DialogType.REQUEST;
import static de.vsy.chat.utility.standard_value.StandardIdProvider.STANDARD_CLIENT_ID;

import de.vsy.chat.transmission.dto.CommunicatorDTO;
import org.apache.logging.log4j.Logger;
import de.vsy.chat.transmission.packet.content.HumanInteractionRequest;
import de.vsy.chat.transmission.packet.content.chat.TextMessageDTO;
import de.vsy.chat.transmission.packet.content.error.ErrorDTO;
import de.vsy.chat.transmission.packet.content.relation.EligibleContactEntity;

import java.util.*;
import org.apache.logging.log4j.LogManager;

/** Manages all dataManagement that originated from server response Packet. */
public class ServerProvidedDataManager {

  private final Logger logger;
  private final ContactDataController activeContactController;
  private final Map<DialogType, Queue<String>> dialogMessages;
  private final MessageController messageController;
  private final List<HumanInteractionRequest> requests;
  private DialogRequestProcessor dialogProcessor;
  private ClientDataManager clientData;

  public ServerProvidedDataManager() {
    this.logger = LogManager.getLogger();
    this.clientData = new ClientDataManager();
    this.activeContactController = new ContactDataController();
    this.messageController = new MessageController();
    this.dialogMessages = new EnumMap<>(DialogType.class);
    this.requests = new ArrayList<>(5);
  }

  /**
   * Adds the active contacts.
   *
   * @param activeContacts the active contacts
   */
  public void addActiveContacts(
      final Map<EligibleContactEntity, Set<CommunicatorDTO>> activeContacts) {
    this.activeContactController.setNewClientList(activeContacts);
  }

  /**
   * Adds the active contact.
   *
   * @param newClient the new client
   * @param messages the messages
   */
  public void addContact(
      final EligibleContactEntity contactType,
      final CommunicatorDTO newClient,
      final List<TextMessageDTO> messages) {
    final var messageHistory = messages != null ? messages : new ArrayList<TextMessageDTO>();

    if (newClient != null) {
      this.activeContactController.addContact(contactType, newClient);
      this.messageController.addMessagesForClient(newClient.getCommunicatorId(), messageHistory);
    }
  }

  /**
   * Adds the dialog message.
   *
   * @param error the error
   */
  public void addDialogMessage(final ErrorDTO error) {

    if (error != null) {
      DialogType type;
      String errorString = null;

      type = INFORMATION;

      errorString = error.getMessage();
      addDialogMessage(type, errorString);
    }
  }

  /**
   * Adds the dialog message.
   *
   * @param type the type
   * @param message the message
   */
  private void addDialogMessage(final DialogType type, final String message) {
    final Queue<String> queuedMessages;
    this.dialogMessages.putIfAbsent(type, new LinkedList<>());

    queuedMessages = this.dialogMessages.get(type);
    queuedMessages.add(message);

    if (this.dialogProcessor != null) {
      this.dialogProcessor.newDialogRequest(type);
    } else {
      String info = null;
      info =
          "**Es wurde kein DialogRequestProcessor ueber die folgende"
              + " Nachricht informiert. Keiner wurde gesetzt.**"
              + "\nNachricht: "
              + message
              + "\nTyp: "
              + type;
      this.logger.info(info);
    }
  }

  /**
   * Adds the dialog message.
   *
   * @param info the info
   */
  public void addDialogMessage(final String info) {
    DialogType type;

    if (info != null) {
      type = INFORMATION;
      addDialogMessage(type, info);
    }
  }

  /**
   * Adds the message.
   *
   * @param msg the msg
   */
  public void addMessage(final TextMessageDTO msg) {
    final var clientId = this.clientData.getCommunicatorDTO().getCommunicatorId();
    final var contactId =
        msg.getRecipientId() == clientId ? msg.getOriginatorId() : msg.getRecipientId();
    this.messageController.addMessage(contactId, msg);
  }

  /**
   * Adds the request.
   *
   * @param request the request
   */
  public void addRequest(final HumanInteractionRequest request) {
    this.requests.add(request);
    addDialogMessage(request);
  }

  /**
   * Adds the dialog message.
   *
   * @param request the request
   */
  private void addDialogMessage(final HumanInteractionRequest request) {
    DialogType type;
    String requestString = null;

    requestString = HumanInteractionRequestTranslator.translate(request);

    if (requestString != null) {
      type = REQUEST;
      addDialogMessage(type, requestString);
    }
  }

  /**
   * Gets the all remaining dialogs.
   *
   * @return the all remaining dialogs
   */
  public Map<DialogType, Queue<String>> getAllRemainingDialogs() {
    return this.dialogMessages;
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
  public int getCommunicatorId() {
    int clientId = STANDARD_CLIENT_ID;

    if (this.clientData != null) {
      final var clientInfo = this.clientData.getCommunicatorDTO();

      if (clientInfo != null) {
        clientId = clientInfo.getCommunicatorId();
      }
    }
    return clientId;
  }

  /**
   * Gets the contact list.
   *
   * @return the contact list
   */
  public Set<CommunicatorDTO> getContactList() {
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
   * Gets the next dialog.
   *
   * @param type the type
   * @return the next dialog
   */
  public String getNextDialog(final DialogType type) {
    Queue<String> messages = this.dialogMessages.get(type);
    String nTextMessage = null;

    if (messages != null) {
      nTextMessage = messages.poll();
    }
    return nTextMessage;
  }

  /**
   * Gets the next request.
   *
   * @return the next request
   */
  public HumanInteractionRequest getNextRequest() {
    HumanInteractionRequest nextRequest = null;

    if (!this.requests.isEmpty()) {
      nextRequest = this.requests.remove(0);
    }
    return nextRequest;
  }

  /**
   * Initial messenger setup.
   *
   * @param newMsgMap the new msg map
   * @param activeContacts the active clients
   */
  public void initialMessengerSetup(
      final Map<Integer, List<TextMessageDTO>> newMsgMap,
      final Map<EligibleContactEntity, Set<CommunicatorDTO>> activeContacts) {
    this.activeContactController.setNewClientList(activeContacts);
    this.messageController.setNewMessageMap(newMsgMap);
  }

  /**
   * Removes the client.
   *
   * @param contactType the client id
   * @param contactData the contact dataManagement
   * @return the active contact
   */
  public boolean removeContact(
      final EligibleContactEntity contactType, final CommunicatorDTO contactData) {
    return this.activeContactController.removeContact(contactType, contactData)
        && this.messageController.removeMessagesForClient(contactData.getCommunicatorId());
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

  /**
   * Sets the dialog request handler.
   *
   * @param dialogProcessor the new dialog request handler
   */
  public void setDialogRequestProcessor(final DialogRequestProcessor dialogProcessor) {
    this.dialogProcessor = dialogProcessor;
  }
}
