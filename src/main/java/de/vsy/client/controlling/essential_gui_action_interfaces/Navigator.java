
package de.vsy.client.controlling.essential_gui_action_interfaces;

import de.vsy.client.gui.essential_graphical_unit.NavigationGoal;

/**
 * Provides GUI elements with limited navigation options.
 */
public interface Navigator {

  /**
   * Initiates the ChatClient shutdown.
   */
  void closeApplication();

  /**
   * Uses specified NavigationGoal which actions to take next.
   *
   * @param status the NavigationGoal
   */
  void navigate(NavigationGoal status);
}
