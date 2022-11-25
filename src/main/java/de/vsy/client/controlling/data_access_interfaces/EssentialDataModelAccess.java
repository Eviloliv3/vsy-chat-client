/*
 *
 */
package de.vsy.client.controlling.data_access_interfaces;

import de.vsy.shared_transmission.packet.content.Translatable;

/**
 * Provides methods for simple dataManagement access and manipulation from all packet handlers.
 */
public interface EssentialDataModelAccess {


  /**
   * Adds the information.
   *
   * @param notification the notification
   */
  void addNotification(final Translatable notification);

  /**
   * Gets the client id.
   *
   * @return the client id
   */
  int getClientId();

  /**
   * Checks whether client is logged in
   *
   * @return is client logged in
   */
  boolean isClientLoggedIn();
}
