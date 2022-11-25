/*
 *
 */
package de.vsy.client.controlling;

import de.vsy.shared_transmission.dto.CommunicatorDTO;

/**
 * Interface for simple client dataManagement access throughout the application.
 */
public interface ClientDataProvider {

  /**
   * Gets the client dataManagement.
   *
   * @return the client dataManagement
   */
  CommunicatorDTO getCommunicatorDTO();
}
