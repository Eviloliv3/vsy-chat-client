/*
 *
 */
package de.vsy.client.controlling.essential_gui_action_interfaces;

import de.vsy.client.gui.essential_graphical_units.NavigationGoal;

/**
 * Provides GUI elements with limited navigation options.
 */
public interface DialogInitNavigation {

  void closeApplication();

  /**
   * Navigate.
   *
   * @param status the status
   */
  void navigate(NavigationGoal status);
}
