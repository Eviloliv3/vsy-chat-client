/*
 *
 */
package de.vsy.chat.client.controlling;

import de.vsy.chat.transmission.dto.CommunicatorDTO;

/** Interface for simple client dataManagement access throughout the application. */
public interface ClientDataProvider {

  /**
   * Gets the client dataManagement.
   *
   * @return the client dataManagement
   */
  public CommunicatorDTO getCommunicatorDTO();
}
