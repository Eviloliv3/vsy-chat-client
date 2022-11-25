/*
 *
 */
package de.vsy.client.controlling.data_access_interfaces;

import de.vsy.shared_transmission.dto.CommunicatorDTO;

/**
 * Provides methods for simple dataManagement manipulation from authentication packet handlers.
 */
public interface AuthenticationDataModelAccess extends EssentialDataModelAccess {

  /**
   * Adds the client dataManagement.
   *
   * @param clientData the client dataManagement
   */
  void addClientData(CommunicatorDTO clientData);

  void completeLogout();

  /**
   * Complete reconnect.
   *
   * @param reconnectionSuccess the reconnection success
   */
  void completeReconnect(boolean reconnectionSuccess);
}
