/*
 *
 */
package de.vsy.client.data_model;

import static de.vsy.shared_utility.standard_value.StandardIdProvider.STANDARD_CLIENT_ID;
import static de.vsy.shared_utility.standard_value.StandardStringProvider.STANDARD_EMPTY_STRING;
import static java.util.Comparator.naturalOrder;

import de.vsy.client.gui.chatter_main_model.GUILabelChangeListener;
import de.vsy.client.gui.essential_graphical_units.MessageHistory;
import de.vsy.shared_transmission.dto.CommunicatorDTO;
import de.vsy.shared_transmission.packet.content.chat.TextMessageDTO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.DefaultListModel;

public class GUIStateManager {

  private final DefaultListModel<CommunicatorDTO> contactListModel;
  private final MessageHistory messages;
  private boolean guiClosedFlag;
  private CommunicatorDTO activeChatContact;
  private CommunicatorDTO activeClient;
  private GUILabelChangeListener labelModifier;

  public GUIStateManager() {
    this.guiClosedFlag = false;
    this.contactListModel = new DefaultListModel<>();
    this.activeClient = CommunicatorDTO.valueOf(STANDARD_CLIENT_ID, STANDARD_EMPTY_STRING);
    this.activeChatContact = CommunicatorDTO.valueOf(STANDARD_CLIENT_ID, STANDARD_EMPTY_STRING);
    this.messages = new MessageHistory();
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
   * Gets the active chat contact.
   *
   * @return the active chat contact
   */
  // -------------------------------------------
  public CommunicatorDTO getActiveChatContact() {
    return this.activeChatContact;
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
      this.activeChatContact = activeChat;
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
    return this.activeClient;
  }

  /**
   * Sets the active client.
   *
   * @param clientLabel the new active client
   */
  public void setActiveClient(final CommunicatorDTO clientLabel) {

    if (clientLabel != null) {
      this.activeClient = clientLabel;
      drawNewClientLabel();
    }
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
   * Removes the contact.
   *
   * @param contactData the contact dataManagement
   */
  public void removeContact(final CommunicatorDTO contactData) {
    this.contactListModel.removeElement(contactData);

    if (this.activeChatContact != null && this.activeChatContact.equals(contactData)) {
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
    setActiveChatContact(CommunicatorDTO.valueOf(STANDARD_CLIENT_ID, STANDARD_EMPTY_STRING));
  }

  private void drawNewChatLabel() {

    if (this.labelModifier != null) {
      this.labelModifier.setChatLabel(this.activeChatContact);
    }
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
    setActiveClient(CommunicatorDTO.valueOf(STANDARD_CLIENT_ID, STANDARD_EMPTY_STRING));
  }

  // -------------------------------------------
  // ---Active-Chat-Contact-&-History-----------
  private void drawNewClientLabel() {

    if (this.labelModifier != null) {
      this.labelModifier.setClientLabel(this.activeClient);
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
   * @param messages    the messages
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

    if (this.activeChatContact != null && message != null) {
      final var contactId = this.activeChatContact.getCommunicatorId();

      if (message.getRecipientId() != contactId) {
        this.messages.addClientMessage(message.getMessage());
      } else {
        this.messages.addContactMessage(message.getMessage());
      }
    }
  }
}
