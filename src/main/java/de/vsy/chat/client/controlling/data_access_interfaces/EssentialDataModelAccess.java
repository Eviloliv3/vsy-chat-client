/*
 *
 */
package de.vsy.chat.client.controlling.data_access_interfaces;

import de.vsy.chat.transmission.packet.content.error.ErrorDTO;

/** Provides methods for simple dataManagement access and manipulation from all packet handlers. */
public interface EssentialDataModelAccess {

  /**
   * Adds the error.
   *
   * @param error the error
   */
  void addError(ErrorDTO error);

  /**
   * Adds the information.
   *
   * @param info the info
   */
  void addInformation(String info);

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
