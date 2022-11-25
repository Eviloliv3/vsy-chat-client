/*
 *
 */
package de.vsy.client.gui.chatter_main_model;

import de.vsy.shared_transmission.dto.CommunicatorDTO;

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
