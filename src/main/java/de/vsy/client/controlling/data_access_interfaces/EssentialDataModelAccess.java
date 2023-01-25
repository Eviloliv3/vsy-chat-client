package de.vsy.client.controlling.data_access_interfaces;

import de.vsy.shared_transmission.dto.CommunicatorDTO;
import de.vsy.shared_transmission.packet.content.Translatable;

/**
 * Provides basic data access appropriate for all Packet handlers.
 */
public interface EssentialDataModelAccess {


  /**
   * Adds the information.
   *
   * @param notification the notification
   */
  void addNotification(final Translatable notification);

  /**
   * Returns the client's id.
   *
   * @return the client id
   */
  int getClientId();

  /**
   * Returns boolean value indicating client's authentication state.
   *
   * @return true if client data set is complete, false otherwise
   */
  boolean isClientLoggedIn();
}
