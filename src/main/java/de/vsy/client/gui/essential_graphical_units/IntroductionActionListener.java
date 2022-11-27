package de.vsy.client.gui.essential_graphical_units;

import de.vsy.client.controlling.essential_gui_action_interfaces.DialogInitNavigation;
import de.vsy.client.gui.essential_graphical_units.prompt.NavigationGoal;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IntroductionActionListener implements ActionListener {

  private final DialogInitNavigation navigator;

  public IntroductionActionListener(final DialogInitNavigation navigator) {
    this.navigator = navigator;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    final NavigationGoal type;
    try {
      final String eventValue = e.getActionCommand().toUpperCase().trim();
      if (eventValue.equals("CLOSE_APPLICATION")) {
        navigator.closeApplication();
      } else {
        type = NavigationGoal.valueOf(e.getActionCommand());
        ((Component) e.getSource()).getFocusCycleRootAncestor().setVisible(false);
        navigator.navigate(type);
      }
    } catch (IllegalArgumentException iae) {
      final var message = "Invalid ActionEvent triggered by " + e.getSource() + ".Allowed types "
          + NavigationGoal.class + " and CLOSE, but got " + e.getActionCommand() + ".";
      throw new RuntimeException(message, iae);
    }
  }
}
