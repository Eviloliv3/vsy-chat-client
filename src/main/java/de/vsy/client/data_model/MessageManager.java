package de.vsy.client.data_model;

import de.vsy.shared_module.data_element_validation.IdCheck;
import de.vsy.shared_transmission.packet.content.chat.TextMessageDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages messages histories for all client's active contacts.
 */
public class MessageManager {

  private static final int MAX_MESSAGE_COUNT = 50;
  private final Map<Integer, List<TextMessageDTO>> messageHistories;

  public MessageManager() {
    this.messageHistories = new HashMap<>();
  }

  /**
   * Adds the messages for client.
   *
   * @param contactId the contact id
   * @param messages  the messages
   */
  public void addMessagesForClient(final int contactId, final List<TextMessageDTO> messages) {
    if (IdCheck.checkData(contactId).isPresent()) {
      return;
    }

    if (messages != null && !(messages.isEmpty())) {
      final int messageCount;
      final var existingMessages = this.messageHistories.getOrDefault(contactId,
          new ArrayList<>(MAX_MESSAGE_COUNT));
      final var allContactMessages = new ArrayList<TextMessageDTO>(
          messages.size() + existingMessages.size());

      allContactMessages.addAll(existingMessages);
      allContactMessages.addAll(messages);
      messageCount = allContactMessages.size();

      if (messageCount > MAX_MESSAGE_COUNT) {
        allContactMessages.subList(0, (messageCount - MAX_MESSAGE_COUNT)).clear();
        allContactMessages.trimToSize();
      }
      this.messageHistories.put(contactId, allContactMessages);
    } else {
      this.messageHistories.putIfAbsent(contactId, new ArrayList<>(MAX_MESSAGE_COUNT));
    }
  }

  /**
   * Adds the message.
   *
   * @param contactId  the contact id
   * @param newMessage the new message
   */
  public void addMessage(final int contactId, final TextMessageDTO newMessage) {
    List<TextMessageDTO> messageList = this.messageHistories.getOrDefault(contactId,
        new ArrayList<>(MAX_MESSAGE_COUNT));
    messageList.add(newMessage);

    if (messageList.size() > MAX_MESSAGE_COUNT) {
      messageList.remove(0);

      if (messageList instanceof ArrayList<TextMessageDTO> messageHistory) {
        messageHistory.trimToSize();
      }
    }
    this.messageHistories.put(contactId, messageList);
  }

  /**
   * Gets the messages.
   *
   * @param contactId the contact id
   * @return the messages
   */
  public List<TextMessageDTO> getMessages(final int contactId) {
    List<TextMessageDTO> messageList;

    if ((messageList = this.messageHistories.get(contactId)) == null) {
      messageList = new ArrayList<>();
    }
    return messageList;
  }

  /**
   * Removes the messages for client.
   *
   * @param contactId the contact id
   */
  public void removeMessagesForClient(final int contactId) {
    this.messageHistories.remove(contactId);
  }

  /**
   * Lists the new message map.
   *
   * @param contactMessages the new map
   */
  public void setNewMessageMap(final Map<Integer, List<TextMessageDTO>> contactMessages) {

    if (contactMessages != null && !(contactMessages.isEmpty())) {

      for (final var messageHistory : contactMessages.entrySet()) {
        addMessagesForClient(messageHistory.getKey(), messageHistory.getValue());
      }
    }
  }
}
