package de.vsy.client.gui.essential_graphical_unit;

import de.vsy.client.controlling.essential_gui_action_interfaces.Navigator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuActionListener implements ActionListener {

  private final Navigator navigator;

  public MenuActionListener(final Navigator navigator) {
    this.navigator = navigator;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    final NavigationGoal type;
    try {
      type = NavigationGoal.valueOf(e.getActionCommand());
      navigator.navigate(type);
    } catch (IllegalArgumentException iae) {
      final var message = "Invalid ActionEvent triggered by " + e.getSource() + ".Allowed types "
          + NavigationGoal.class + " and CLOSE, but got " + e.getActionCommand() + ".";
      throw new RuntimeException(message, iae);
    }
  }
}
