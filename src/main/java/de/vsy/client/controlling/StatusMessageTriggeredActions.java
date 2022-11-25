/*
 *
 */
package de.vsy.client.controlling;

import de.vsy.shared_transmission.dto.CommunicatorDTO;
import de.vsy.shared_transmission.packet.content.chat.TextMessageDTO;

/**
 * Listener interface for certain server response Packet that require controller actions.
 */
public interface StatusMessageTriggeredActions {

  /**
   * Contact online status change.
   *
   * @param contact the contact
   * @param status  the status
   */
  void changeContactStatus(CommunicatorDTO contact, boolean status);

  void completeLogin();

  void completeLogout();

  /**
   * Message received.
   *
   * @param message the message
   */
  void messageReceived(TextMessageDTO message);

  void completeMessengerSetup();

  /**
   * Messenger tear down complete.
   */
  void completeMessengerTearDown();

  /**
   * Reconnect failed.
   */
  void reconnectFailed();
}
