package de.vsy.client.data_model;

import de.vsy.client.gui.essential_graphical_unit.MessageHistory;
import de.vsy.shared_module.data_element_validation.IdCheck;
import de.vsy.shared_transmission.packet.content.chat.TextMessageDTO;
import java.security.DrbgParameters.NextBytes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.core.tools.picocli.CommandLine.Help.Ansi.Text;

/**
 * Manages messages histories for all client's active contacts.
 *
 * <p>Frederic Heath
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
   * @param messages the messages
   */
  public void addMessagesForClient(final int contactId, final List<TextMessageDTO> messages) {
    if (IdCheck.checkData(contactId).isEmpty()) {
      return;
    }

    if(messages != null && !(messages.isEmpty())){
      final int messageCount;
      final var existingMessages = this.messageHistories.getOrDefault(contactId, new ArrayList<>(MAX_MESSAGE_COUNT));
      final var allContactMessages = new ArrayList<TextMessageDTO>(messages.size() + existingMessages.size());

      allContactMessages.addAll(existingMessages);
      allContactMessages.addAll(messages);
      messageCount = allContactMessages.size();

      if(messageCount > MAX_MESSAGE_COUNT){
        allContactMessages.subList(0, (messageCount - MAX_MESSAGE_COUNT)).clear();
        allContactMessages.trimToSize();
      }
      this.messageHistories.put(contactId, existingMessages);
    }else{
      this.messageHistories.putIfAbsent(contactId, new ArrayList<>(MAX_MESSAGE_COUNT));
    }
  }

  /**
   * Adds the message.
   *
   * @param clientId   the client id
   * @param newMessage the new message
   */
  public void addMessage(final int clientId, final TextMessageDTO newMessage) {
    List<TextMessageDTO> messageList = this.messageHistories.getOrDefault(clientId, new ArrayList<>(MAX_MESSAGE_COUNT));
    messageList.add(newMessage);

    if (messageList.size() > MAX_MESSAGE_COUNT) {
      messageList.remove(0);

      if(messageList instanceof ArrayList<TextMessageDTO> messageHistory){
        messageHistory.trimToSize();
      }
    }
    this.messageHistories.put(clientId, messageList);
  }

  /**
   * Gets the messages.
   *
   * @param clientId the client id
   * @return the messages
   */
  public List<TextMessageDTO> getMessages(final int clientId) {
    List<TextMessageDTO> messageList;

    if ((messageList = this.messageHistories.get(clientId)) == null) {
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
    this.messageHistories.remove(clientId);
  }

  /**
   * Lists the new message map.
   *
   * @param contactMessages the new map
   */
  public void setNewMessageMap(final Map<Integer, List<TextMessageDTO>> contactMessages) {

    if(contactMessages != null && !(contactMessages.isEmpty())){

      for(final var messageHistory : contactMessages.entrySet()){
        addMessagesForClient(messageHistory.getKey(), messageHistory.getValue());
      }
    }
  }
}
