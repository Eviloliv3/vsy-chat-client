/*
 *
 */
package de.vsy.chat.client.controlling.essential_gui_action_interfaces.guiActionInterfaces;

import java.awt.event.MouseEvent;

/** Provides GUI base frame with capabilities of notifiying the GUI logic of client actions. */
public interface GUIChatActions {

  /**
   * Choose contact.
   *
   * @param evt the evt
   */
  public void chooseContact(MouseEvent evt);

  public void sendMessage();
}
