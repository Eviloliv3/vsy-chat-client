package de.vsy.client.gui;

import de.vsy.client.data_model.ClientInput;
import de.vsy.shared_transmission.dto.CommunicatorDTO;

public interface ClientInputProvider {

  /**
   * Returns the ClientInput containing a String message.
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
