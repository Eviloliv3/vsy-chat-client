/*
 *
 */
package de.vsy.client.gui.essential_graphical_units;


import static de.vsy.client.gui.essential_graphical_units.prompt.NavigationGoal.ACCOUNT_CREATION;
import static de.vsy.client.gui.essential_graphical_units.prompt.NavigationGoal.CONTACT_ADDITION;
import static de.vsy.client.gui.essential_graphical_units.prompt.NavigationGoal.CONTACT_REMOVAL;
import static de.vsy.client.gui.essential_graphical_units.prompt.NavigationGoal.LOGIN;
import static de.vsy.client.gui.essential_graphical_units.prompt.NavigationGoal.LOGOUT;

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
  private JMenuItem addContactMenuButton;
  private JMenu chatterOptions;
  private JMenu contactOptions;
  private JMenuItem loginMenuButton;
  private JMenuItem logoutMenuButton;
  private JMenuItem newAccountMenuButton;
  private JMenuItem removeContactMenuButton;

  /**
   * Instantiates a new chatter menu.
   */
  public ChatterMenu(final ActionListener ac) {
    initComponents(ac);
  }

  private void initComponents(final ActionListener ac) {
    this.chatterOptions = new JMenu("Options");
    this.contactOptions = new JMenu("Contacts");

    this.loginMenuButton = new JMenuItem("Login");
    this.newAccountMenuButton = new JMenuItem("Create Account");
    this.logoutMenuButton = new JMenuItem("Logout");

    this.addContactMenuButton = new JMenuItem("Add Contact");
    this.removeContactMenuButton = new JMenuItem("Remove Contact");

    this.loginMenuButton.setActionCommand(String.valueOf(LOGIN));
    this.loginMenuButton.addActionListener(ac);

    this.newAccountMenuButton.setActionCommand(String.valueOf(ACCOUNT_CREATION));
    this.newAccountMenuButton.addActionListener(ac);

    this.logoutMenuButton.setActionCommand(String.valueOf(LOGOUT));
    this.logoutMenuButton.addActionListener(ac);

    this.addContactMenuButton.setActionCommand(String.valueOf(CONTACT_ADDITION));
    this.addContactMenuButton.addActionListener(ac);

    this.removeContactMenuButton.setActionCommand(String.valueOf(CONTACT_REMOVAL));
    this.removeContactMenuButton.addActionListener(ac);

    this.chatterOptions.add(this.loginMenuButton);
    this.chatterOptions.add(this.newAccountMenuButton);
    this.chatterOptions.add(this.logoutMenuButton);

    this.contactOptions.add(this.addContactMenuButton);
    this.contactOptions.add(this.removeContactMenuButton);

    add(this.chatterOptions);
    add(this.contactOptions);
  }
}
