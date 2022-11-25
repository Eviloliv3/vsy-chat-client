/*
 *
 */
package de.vsy.client.controlling.data_access_interfaces;

import de.vsy.shared_transmission.packet.content.chat.TextMessageDTO;

/**
 * TProvides methods for simple dataManagement manipulation from chat packet handlers.
 */
public interface ChatDataModelAccess extends EssentialDataModelAccess {

  /**
   * Adds the message.
   *
   * @param message the message
   */
  void addMessage(TextMessageDTO message);
}
