/*
 *
 */
package de.vsy.chat.client.gui.chatter_main_model;

import de.vsy.chat.transmission.dto.CommunicatorDTO;

public interface GUILogicRelevantDataProvider {

  /**
   * Gets the message.
   *
   * @return the message
   */
  String getMessage();

  /**
   * Gets the selected contact.
   *
   * @return the selected contact
   */
  CommunicatorDTO getSelectedContact();
}
