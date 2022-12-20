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
//TODO too cluttered -> Interface should present specific, contained set of actions
// to be provided by implementing classes! This is just a collection of functions
// implemented by a specific class (ChatClientController) that is passed into InputController
// objects
// => sort out Controller situation!

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
