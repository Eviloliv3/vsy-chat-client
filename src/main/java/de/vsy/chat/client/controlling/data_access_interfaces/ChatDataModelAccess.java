/*
 *
 */
package de.vsy.chat.client.controlling.data_access_interfaces;

import de.vsy.chat.transmission.packet.content.chat.TextMessageDTO;

/** TProvides methods for simple dataManagement manipulation from chat packet handlers. */
public interface ChatDataModelAccess extends EssentialDataModelAccess {

  /**
   * Adds the message.
   *
   * @param message the message
   */
  public void addMessage(TextMessageDTO message);
}
