/*
 *
 */
package de.vsy.chat.client.data_model;

import de.vsy.chat.client.gui.chatter_main_model.GUILabelChangeListener;
import de.vsy.chat.client.gui.essential_graphical_units.MessageHistory;
import de.vsy.chat.client.gui.essential_graphical_units.dialog.ChatClientDialog;
import de.vsy.chat.client.gui.essential_graphical_units.dialog.DialogType;
import de.vsy.chat.transmission.dto.CommunicatorDTO;
import de.vsy.chat.transmission.packet.content.chat.TextMessageDTO;

import javax.swing.*;
import java.util.*;

import static de.vsy.chat.utility.standard_value.StandardIdProvider.STANDARD_CLIENT_ID;
import static de.vsy.chat.utility.standard_value.StandardStringProvider.STANDARD_EMTPY_STRING;
import static java.util.Comparator.naturalOrder;

public class GUIStateDataManager {

  private final Map<DialogType, ChatClientDialog> activeDialogs;
  private final DefaultListModel<CommunicatorDTO> contactListModel;
  private boolean guiClosedFlag;
  private final MessageHistory messages;
  private final Map<DialogType, Queue<ChatClientDialog>> pendingDialogs;
  private CommunicatorDTO activeChat;
  private CommunicatorDTO activeContact;
  private GUILabelChangeListener labelModifier;

  public GUIStateDataManager() {
    this.guiClosedFlag = false;
    this.contactListModel = new DefaultListModel<>();
    this.activeContact = CommunicatorDTO.valueOf(STANDARD_CLIENT_ID, STANDARD_EMTPY_STRING);
    this.activeChat = CommunicatorDTO.valueOf(STANDARD_CLIENT_ID, STANDARD_EMTPY_STRING);
    this.messages = new MessageHistory();
    this.activeDialogs = new EnumMap<>(DialogType.class);
    this.pendingDialogs = new EnumMap<>(DialogType.class);
  }

  /**
   * Adds the dialog.
   *
   * @param newDialog the new dialog
   * @return true, if successful
   */
  public boolean addActiveDialog(final ChatClientDialog newDialog) {
    final var type = newDialog.getDialogType();

    return this.activeDialogs.putIfAbsent(type, newDialog) == null;
  }

  /**
   * Adds the contact.
   *
   * @param contactData the contact dataManagement
   */
  public void addContact(final CommunicatorDTO contactData) {
    List<CommunicatorDTO> list = Collections.list(this.contactListModel.elements());
    list.add(contactData);
    list = sortByFirstLastName(list);

    clearContactList();
    this.contactListModel.addAll(list);
  }

  /**
   * Sort by first last name.
   *
   * @param activeContactList the active clients
   * @return the default list model
   */
  private List<CommunicatorDTO> sortByFirstLastName(final List<CommunicatorDTO> activeContactList) {
    List<CommunicatorDTO> sortedListModel;

    if (activeContactList != null) {
      if (activeContactList.size() > 1) {
        sortedListModel = new ArrayList<>(activeContactList.size());
        sortedListModel.addAll(activeContactList);
        sortedListModel.sort(naturalOrder());
      } else {
        sortedListModel = activeContactList;
      }
    } else {
      sortedListModel = new ArrayList<>();
    }
    return sortedListModel;
  }

  public void clearContactList() {
    this.contactListModel.removeAllElements();
  }

  /**
   * Adds the pending dialog.
   *
   * @param pendingDialog the pending dialog
   */
  public void addPendingDialog(final ChatClientDialog pendingDialog) {
    final var type = pendingDialog.getDialogType();
    Queue<ChatClientDialog> pendingDialogs;

    pendingDialogs = this.pendingDialogs.get(type);

    if (pendingDialogs == null) {
      pendingDialogs = new LinkedList<>();
    }

    pendingDialogs.add(pendingDialog);

    this.pendingDialogs.put(type, pendingDialogs);
  }

  /**
   * Gets the active chat contact.
   *
   * @return the active chat contact
   */
  // -------------------------------------------
  public CommunicatorDTO getActiveChatContact() {
    return this.activeChat;
  }

  // -------------------------------------------
  // ---Active-Chat-----------------------------
  /**
   * Sets the active chat contact.
   *
   * @param activeChat the new active chat contact
   */
  public void setActiveChatContact(final CommunicatorDTO activeChat) {

    if (activeChat != null) {
      this.activeChat = activeChat;
      drawNewChatLabel();
    }
  }

  // -------------------------------------------
  // ---Active-Chat-History---------------------
  /**
   * Gets the active chat history.
   *
   * @return the active chat history
   */
  // -------------------------------------------
  public MessageHistory getActiveChatHistory() {
    return this.messages;
  }

  // -------------------------------------------
  // ---Client-Data-----------------------------
  /**
   * Gets the active client.
   *
   * @return the active client
   */
  // -------------------------------------------
  public CommunicatorDTO getActiveClient() {
    return this.activeContact;
  }

  /**
   * Sets the active client.
   *
   * @param clientLabel the new active client
   */
  public void setActiveClient(final CommunicatorDTO clientLabel) {

    if (clientLabel != null) {
      this.activeContact = clientLabel;
      drawNewClientLabel();
    }
  }

  /**
   * Gets the all dialogs.
   *
   * @return the all dialogs
   */
  public Map<DialogType, ChatClientDialog> getAllDialogs() {
    return this.activeDialogs;
  }

  // -------------------------------------------
  // ---Contact-List----------------------------
  // -------------------------------------------
  /**
   * Gets the all pending dialogs.
   *
   * @return the all pending dialogs
   */
  public Map<DialogType, Queue<ChatClientDialog>> getAllPendingDialogs() {
    return this.pendingDialogs;
  }

  // -------------------------------------------
  // ---Active-Dialogs--------------------------
  /**
   * Gets the contact list model.
   *
   * @return the contact list model
   */
  public DefaultListModel<CommunicatorDTO> getContactListModel() {
    return this.contactListModel;
  }

  // -------------------------------------------
  // ---GUI-Closed-Flag-------------------------
  /**
   * Gets the dialog.
   *
   * @param type the type
   * @return the dialog
   */
  // -------------------------------------------
  public ChatClientDialog getDialog(final DialogType type) {
    return this.activeDialogs.get(type);
  }

  /**
   * Gets the GUI state flag.
   *
   * @return the GUI state flag
   */
  // -------------------------------------------
  public boolean getGUIStateFlag() {
    return this.guiClosedFlag;
  }

  /**
   * Sets the GUI state flag.
   *
   * @param newState the new GUI state flag
   */
  public void setGUIStateFlag(final boolean newState) {
    this.guiClosedFlag = newState;
  }

  /**
   * Gets the next pending dialog.
   *
   * @param type the type
   * @return the next pending dialog
   */
  public ChatClientDialog getNextPendingDialog(final DialogType type) {
    ChatClientDialog nextPending = null;
    final var pendingDialogs = this.pendingDialogs.get(type);

    if (pendingDialogs != null) {
      nextPending = pendingDialogs.poll();
    }

    return nextPending;
  }

  /**
   * Removes the contact.
   *
   * @param contactData the contact dataManagement
   */
  public void removeContact(final CommunicatorDTO contactData) {
    this.contactListModel.removeElement(contactData);

    if (this.activeChat != null && this.activeChat.equals(contactData)) {
      clearActiveChat();
    }
  }

  public void clearActiveChat() {
    removeActiveChatContact();
    this.messages.clearMessageHistory();
  }

  // -------------------------------------------
  // ---Helper-Methods--------------------------
  public void removeActiveChatContact() {
    setActiveChatContact(CommunicatorDTO.valueOf(STANDARD_CLIENT_ID, STANDARD_EMTPY_STRING));
  }

  private void drawNewChatLabel() {

    if (this.labelModifier != null) {
      this.labelModifier.setNewChatLabel(this.activeChat);
    }
  }

  /**
   * Removes the dialog.
   *
   * @param type the type
   * @return true, if successful
   */
  public boolean removeDialog(final DialogType type) {
    return this.activeDialogs.remove(type) != null;
  }

  // -------------------------------------------
  // ---GUI-Label-Listener----------------------
  // -------------------------------------------
  public void resetAllData() {
    removeActiveClient();
    clearActiveChat();
    clearContactList();
  }

  public void removeActiveClient() {
    setActiveClient(CommunicatorDTO.valueOf(STANDARD_CLIENT_ID, STANDARD_EMTPY_STRING));
  }

  // -------------------------------------------
  // ---Active-Chat-Contact-&-History-----------
  private void drawNewClientLabel() {

    if (this.labelModifier != null) {
      this.labelModifier.setNewClientLabel(this.activeContact);
    }
  }

  /**
   * Sets the GUI label change listener.
   *
   * @param listener the new GUI label change listener
   */
  // -------------------------------------------
  public void setGUILabelChangeListener(final GUILabelChangeListener listener) {

    if (listener != null) {
      this.labelModifier = listener;
    }
  }

  /**
   * Sets the new active chat.
   *
   * @param contactData the contact dataManagement
   * @param messages the messages
   */
  // -------------------------------------------
  public void setNewActiveChat(
      final CommunicatorDTO contactData, final List<TextMessageDTO> messages) {

    if (contactData != null && messages != null) {
      setActiveChatContact(contactData);
      this.messages.clearMessageHistory();
      drawMessages(messages);
    }
  }

  /**
   * Draw messages.
   *
   * @param messages the messages
   */
  private void drawMessages(final List<TextMessageDTO> messages) {

    for (var currentMessage : messages) {
      addMessage(currentMessage);
    }
  }

  /**
   * Adds the message.
   *
   * @param message the message
   */
  public void addMessage(final TextMessageDTO message) {

    if (this.activeChat != null && message != null) {
      final var contactId = this.activeChat.getCommunicatorId();

      if (message.getRecipientId() != contactId) {
        this.messages.addClientMessage(message.getMessage());
      } else {
        this.messages.addContactMessage(message.getMessage());
      }
    }
  }
}
