/*
 *
 */
package de.vsy.chat.client.gui.essential_graphical_units;


import javax.swing.*;

import de.vsy.chat.client.controlling.essential_gui_action_interfaces.GUIMenuActions;

/**
 * Provides a standard menu bar for the Java Chatter Application.
 */
public
class ChatterMenu extends JMenuBar {

    private static final long serialVersionUID = -2264168742233506774L;
    private final transient GUIMenuActions menuActionController;
    private JMenuItem addContactMenuButton;
    private JMenu chatterOptions;
    private JMenu contactOptions;
    private JMenuItem loginMenuButton;
    private JMenuItem logoutMenuButton;
    private JMenuItem newAccountMenuButton;
    private JMenuItem removeContactMenuButton;

    /**
     * Instantiates a new chatter menu.
     *
     * @param menuController the menu controller
     */
    public
    ChatterMenu (final GUIMenuActions menuController) {
        this.menuActionController = menuController;
        initComponents();
    }

    private
    void initComponents () {
        this.chatterOptions = new JMenu("Optionen");
        this.contactOptions = new JMenu("Kontakte");

        this.loginMenuButton = new JMenuItem("Login");
        this.newAccountMenuButton = new JMenuItem("Account erstellen");
        this.logoutMenuButton = new JMenuItem("Logout");

        this.addContactMenuButton = new JMenuItem("Hinzufuegen");
        this.removeContactMenuButton = new JMenuItem("Entfernen");

        this.loginMenuButton.addActionListener(
                (final var evt) -> this.menuActionController.login());

        this.newAccountMenuButton.addActionListener(
                (final var evt) -> this.menuActionController.createAccount());

        this.logoutMenuButton.addActionListener(
                (final var evt) -> this.menuActionController.logout());

        this.addContactMenuButton.addActionListener(
                (final var evt) -> this.menuActionController.addContact());

        this.removeContactMenuButton.addActionListener(
                (final var evt) -> this.menuActionController.removeContact());

        this.chatterOptions.add(this.loginMenuButton);
        this.chatterOptions.add(this.newAccountMenuButton);
        this.chatterOptions.add(this.logoutMenuButton);

        this.contactOptions.add(this.addContactMenuButton);
        this.contactOptions.add(this.removeContactMenuButton);

        add(this.chatterOptions);
        add(this.contactOptions);
    }
}
