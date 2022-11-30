/*
 *
 */
package de.vsy.client.data_model;

import de.vsy.shared_module.data_element_validation.IdCheck;
import de.vsy.shared_transmission.packet.content.chat.TextMessageDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages messages histories for all client's active contacts.
 *
 * <p>Frederic Heath
 */
public class MessageManager {

  private final Map<Integer, List<TextMessageDTO>> oldMessages;

  public MessageManager() {
    this(new HashMap<>());
  }

  /**
   * Instantiates a new message controller.
   *
   * @param newMessageMap the new message map
   */
  public MessageManager(final Map<Integer, List<TextMessageDTO>> newMessageMap) { // NO_UCD
    // (use
    // private)

    if (!newMessageMap.isEmpty()) {
      this.oldMessages = newMessageMap;
    } else {
      this.oldMessages = new HashMap<>();
    }
  }

  /**
   * Adds the messages for client.
   *
   * @param clientId the client id
   * @param messages the messages
   */
  public void addMessagesForClient(final int clientId, final List<TextMessageDTO> messages) {

    if (IdCheck.checkData(clientId).isPresent() && messages != null) {

      if (!(this.oldMessages.containsKey(clientId))) {
        this.oldMessages.put(clientId, messages);
      } else {

        for (var currentMessage : messages) {
          if (currentMessage != null) {
            addMessage(clientId, currentMessage);
          }
        }
      }
    }
  }

  /**
   * Adds the message.
   *
   * @param clientId   the client id
   * @param newMessage the new message
   */
  public void addMessage(final int clientId, final TextMessageDTO newMessage) {
    List<TextMessageDTO> messageList;

    if ((messageList = this.oldMessages.get(clientId)) == null) {
      messageList = new ArrayList<>();
    }

    if (messageList.size() >= 50) {
      final var messageListCopy = new ArrayList<>(messageList);
      messageListCopy.remove(0);
      messageList.clear();
      messageList.addAll(messageListCopy);
    }
    messageList.add(newMessage);
    this.oldMessages.put(clientId, messageList);
  }

  /**
   * Gets the messages.
   *
   * @param clientId the client id
   * @return the messages
   */
  public List<TextMessageDTO> getMessages(final int clientId) {
    List<TextMessageDTO> messageList;

    if ((messageList = this.oldMessages.get(clientId)) == null) {
      messageList = new ArrayList<>();
    }

    return messageList;
  }

  /**
   * Removes the messages for client.
   *
   * @param clientId the client id
   */
  public void removeMessagesForClient(final int clientId) {
    this.oldMessages.remove(clientId);
  }

  /**
   * Lists the new message map.
   *
   * @param newMap the new map
   */
  public void setNewMessageMap(final Map<Integer, List<TextMessageDTO>> newMap) {
    this.oldMessages.putAll(newMap);
  }
}
