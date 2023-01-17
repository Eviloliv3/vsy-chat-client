package de.vsy.client.controlling.essential_gui_action_interfaces.guiActionInterfaces;

import java.awt.event.MouseEvent;

/**
 * Provides GUI base frame with capabilities of notifying the GUI logic of client actions.
 */
public interface GUIChatActions {

  /**
   * Uses Mouseevent to determine chosen contact.
   *
   * @param evt the triggering MouseEvent
   */
  void chooseContact(MouseEvent evt);

  /**
   * Triggers input message processing.
   */
  void sendMessage();
}
