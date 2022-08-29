/*
 *
 */
package de.vsy.chat.client.gui.essential_graphical_units.dialog;

import de.vsy.chat.client.controlling.essential_gui_action_interfaces.DialogInitNavigation;
import de.vsy.chat.transmission.packet.content.PacketContent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static de.vsy.chat.client.gui.essential_graphical_units.dialog.DialogStatus.ACCOUNT_CREATION;
import static de.vsy.chat.client.gui.essential_graphical_units.dialog.DialogStatus.LOGIN;
import static de.vsy.chat.client.gui.essential_graphical_units.dialog.DialogType.INITIALIZATION;
import static java.lang.Short.MAX_VALUE;
import static javax.swing.GroupLayout.Alignment.LEADING;
import static javax.swing.GroupLayout.Alignment.TRAILING;
import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;
import static javax.swing.LayoutStyle.ComponentPlacement.UNRELATED;

public class WelcomeDialog extends ChatClientDialog {

  private static final long serialVersionUID = 4922916412599991228L;
  private final DialogInitNavigation navigator;
  private JButton closeApplicationButton;
  private JButton welcomeLoginButton;
  private JButton welcomeNewAccButton;

  /**
   * Instantiates a new welcome dialog.
   *
   * @param navigator the navigator
   */
  public WelcomeDialog(final DialogInitNavigation navigator) {
    super(INITIALIZATION);
    this.navigator = navigator;
    initComponents();
  }

  private void initComponents() {
    this.welcomeLoginButton = new JButton();
    this.welcomeNewAccButton = new JButton();
    this.closeApplicationButton = new JButton();

    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setTitle("Chatter - Willkommen");
    setAlwaysOnTop(true);
    setLocation(new Point(0, 0));
    setMinimumSize(new Dimension(260, 160));
    setModal(true);
    setSize(new Dimension(260, 160));
    setResizable(false);

    this.welcomeLoginButton.setText("Einloggen");
    this.welcomeLoginButton.setMaximumSize(new Dimension(138, 25));
    this.welcomeLoginButton.setMinimumSize(new Dimension(138, 25));
    this.welcomeLoginButton.setPreferredSize(new Dimension(138, 25));
    this.welcomeLoginButton.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(final MouseEvent evt) {
            navigator.navigate(LOGIN);
          }
        });

    this.welcomeNewAccButton.setText("Neuer Account");
    this.welcomeNewAccButton.setPreferredSize(new Dimension(138, 25));
    this.welcomeNewAccButton.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(final MouseEvent evt) {
            navigator.navigate(ACCOUNT_CREATION);
          }
        });

    this.closeApplicationButton.setText("Beenden");
    this.closeApplicationButton.setMaximumSize(new Dimension(138, 25));
    this.closeApplicationButton.setMinimumSize(new Dimension(138, 25));
    this.closeApplicationButton.setPreferredSize(new Dimension(138, 25));
    this.closeApplicationButton.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(final MouseEvent evt) {
            closeDialogAndApplication();
          }
        });

    final var welcomeDialogLayout = new GroupLayout(getContentPane());
    getContentPane().setLayout(welcomeDialogLayout);
    welcomeDialogLayout.setHorizontalGroup(
        welcomeDialogLayout
            .createParallelGroup(LEADING)
            .addGroup(
                welcomeDialogLayout
                    .createSequentialGroup()
                    .addGap(53, 53, 53)
                    .addGroup(
                        welcomeDialogLayout
                            .createParallelGroup(LEADING)
                            .addComponent(
                                this.closeApplicationButton, PREFERRED_SIZE, 138, PREFERRED_SIZE)
                            .addComponent(
                                this.welcomeNewAccButton, PREFERRED_SIZE, 138, PREFERRED_SIZE)
                            .addComponent(
                                this.welcomeLoginButton, PREFERRED_SIZE, 138, PREFERRED_SIZE))
                    .addContainerGap(67, MAX_VALUE)));
    welcomeDialogLayout.setVerticalGroup(
        welcomeDialogLayout
            .createParallelGroup(LEADING)
            .addGroup(
                TRAILING,
                welcomeDialogLayout
                    .createSequentialGroup()
                    .addGap(15, 15, 15)
                    .addComponent(
                        this.welcomeLoginButton, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                    .addPreferredGap(UNRELATED)
                    .addComponent(
                        this.welcomeNewAccButton, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(
                        this.closeApplicationButton, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                    .addContainerGap(40, MAX_VALUE)));
  }

  private void closeDialogAndApplication() {
    this.navigator.closeApplication();
    dispose();
  }

  @Override
  public PacketContent evaluate() {
    return null;
  }
}
