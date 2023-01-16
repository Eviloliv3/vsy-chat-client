
package de.vsy.client.controlling.data_access_interfaces;

import de.vsy.shared_transmission.packet.content.chat.TextMessageDTO;

/**
 * Provides CHAT related Packet handlers with appropriate data access.
 */
public interface ChatDataModelAccess extends EssentialDataModelAccess {

  /**
   * Adds the specified TextMessageDTO to local storage and makes sure, that it gets displayed if
   * necessary.
   *
   * @param message the message
   */
  void addMessage(TextMessageDTO message);
}
