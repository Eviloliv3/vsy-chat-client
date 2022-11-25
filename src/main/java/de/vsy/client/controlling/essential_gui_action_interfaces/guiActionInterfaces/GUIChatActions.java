/*
 *
 */
package de.vsy.client.controlling.essential_gui_action_interfaces.guiActionInterfaces;

import java.awt.event.MouseEvent;

/**
 * Provides GUI base frame with capabilities of notifying the GUI logic of client actions.
 */
public interface GUIChatActions {

  /**
   * Choose contact.
   *
   * @param evt the evt
   */
  void chooseContact(MouseEvent evt);

  void sendMessage();
}
