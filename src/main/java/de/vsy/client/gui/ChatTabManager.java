package de.vsy.client.gui;

import de.vsy.client.gui.essential_graphical_unit.MessageHistory;
import de.vsy.shared_transmission.dto.CommunicatorDTO;

public interface ChatTabManager {

  /**
   * Checks whether a chat history already exists for the specified contact.
   * @param contact the contact CommunicatorDTO to check
   * @return  true, if active chat history was found; false otherwise
   */
  boolean checkActiveChat(final CommunicatorDTO contact);

  /**
   * Adds the specified chat history for the specified contact data to the GUI.
   *
   * @param contact     the contact's CommunicatorDTO
   * @param chatHistory the client's MessageHistory with the contact
   */
  void addActiveChat(final CommunicatorDTO contact, MessageHistory chatHistory);

  /**
   * Removes the chat history for the specified contact data from the GUI.
   *
   * @param contact
   */
  void removeActiveChat(final CommunicatorDTO contact);
}
