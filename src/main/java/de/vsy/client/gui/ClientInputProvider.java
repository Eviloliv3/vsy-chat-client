/*
 *
 */
package de.vsy.client.gui;

import de.vsy.client.data_model.ClientInput;
import de.vsy.shared_transmission.dto.CommunicatorDTO;

public interface ClientInputProvider {

  /**
   * Gets the message.
   *
   * @return the message
   */
  ClientInput<String> getInput();

  /**
   * Gets the selected contact.
   *
   * @return the selected contact
   */
  CommunicatorDTO getSelectedContact();
}
