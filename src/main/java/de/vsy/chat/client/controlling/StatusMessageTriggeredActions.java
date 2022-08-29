/*
 *
 */
package de.vsy.chat.client.controlling;

import de.vsy.chat.transmission.dto.CommunicatorDTO;
import de.vsy.chat.transmission.packet.content.chat.TextMessageDTO;

/** Listener interface for certain server response Packet that require controller actions. */
public interface StatusMessageTriggeredActions {

  /**
   * Contact online status change.
   *
   * @param contact the contact
   * @param status the status
   */
  public void contactOnlineStatusChange(CommunicatorDTO contact, boolean status);

  public void loginComplete();

  public void logoutComplete();

  /**
   * Message received.
   *
   * @param message the message
   */
  public void messageReceived(TextMessageDTO message);

  public void messengerSetupComplete();

  /** Messenger tear down complete. */
  public void messengerTearDownComplete();

  /** Reconnect failed. */
  public void reconnectFailed();
}
