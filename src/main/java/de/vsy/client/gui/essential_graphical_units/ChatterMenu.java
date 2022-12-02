/*
 *
 */
package de.vsy.client.gui.essential_graphical_units;


import static de.vsy.client.gui.essential_graphical_units.NavigationGoal.ACCOUNT_CREATION;
import static de.vsy.client.gui.essential_graphical_units.NavigationGoal.CONTACT_ADDITION;
import static de.vsy.client.gui.essential_graphical_units.NavigationGoal.CONTACT_REMOVAL;
import static de.vsy.client.gui.essential_graphical_units.NavigationGoal.LOGIN;
import static de.vsy.client.gui.essential_graphical_units.NavigationGoal.LOGOUT;

import java.awt.event.ActionListener;
import java.io.Serial;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * Provides a standard menu bar for the Java Chatter Application.
 */
public
class ChatterMenu extends JMenuBar {

  @Serial
  private static final long serialVersionUID = -2264168742233506774L;

  /**
   * Instantiates a new chatter menu.
   */
  public ChatterMenu(final ActionListener ac) {
    initComponents(ac);
  }

  private void initComponents(final ActionListener ac) {
    JMenu chatterOptions = new JMenu("Options");
    JMenu contactOptions = new JMenu("Contacts");

    JMenuItem loginMenuButton = new JMenuItem("Login");
    JMenuItem newAccountMenuButton = new JMenuItem("Create Account");
    JMenuItem logoutMenuButton = new JMenuItem("Logout");

    JMenuItem addContactMenuButton = new JMenuItem("Add Contact");
    JMenuItem removeContactMenuButton = new JMenuItem("Remove Contact");

    loginMenuButton.setActionCommand(String.valueOf(LOGIN));
    loginMenuButton.addActionListener(ac);

    newAccountMenuButton.setActionCommand(String.valueOf(ACCOUNT_CREATION));
    newAccountMenuButton.addActionListener(ac);

    logoutMenuButton.setActionCommand(String.valueOf(LOGOUT));
    logoutMenuButton.addActionListener(ac);

    addContactMenuButton.setActionCommand(String.valueOf(CONTACT_ADDITION));
    addContactMenuButton.addActionListener(ac);

    removeContactMenuButton.setActionCommand(String.valueOf(CONTACT_REMOVAL));
    removeContactMenuButton.addActionListener(ac);

    chatterOptions.add(loginMenuButton);
    chatterOptions.add(newAccountMenuButton);
    chatterOptions.add(logoutMenuButton);

    contactOptions.add(addContactMenuButton);
    contactOptions.add(removeContactMenuButton);

    add(chatterOptions);
    add(contactOptions);
  }
}
