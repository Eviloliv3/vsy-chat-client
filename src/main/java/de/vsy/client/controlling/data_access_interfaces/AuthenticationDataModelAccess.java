
package de.vsy.client.controlling.data_access_interfaces;

import de.vsy.shared_transmission.dto.CommunicatorDTO;

/**
 * Provides AUTHENTICATION related Packet handlers with appropriate data access.
 */
public interface AuthenticationDataModelAccess extends EssentialDataModelAccess {

  /**
   * Uses client's CommunicatorDTO to initiate ChatClient setup.
   *
   * @param clientData the CommunicatorDTO
   */
  void completeLogin(CommunicatorDTO clientData);

  /**
   * Removes all client related data.
   */
  void completeLogout();

  /**
   * Completes reconnection after connection loss, possibly resetting the client.
   *
   * @param reconnectionSuccess the reconnection success
   */
  void completeReconnect(boolean reconnectionSuccess);
}
