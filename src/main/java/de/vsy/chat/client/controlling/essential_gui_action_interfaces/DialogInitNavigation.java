/*
 *
 */
package de.vsy.chat.client.controlling.essential_gui_action_interfaces;

import de.vsy.chat.client.gui.essential_graphical_units.dialog.DialogStatus;

/** Provides GUI elements with limited navigation options. */
public interface DialogInitNavigation {

  public void closeApplication();

  /**
   * Navigate.
   *
   * @param status the status
   */
  public void navigate(DialogStatus status);
}
