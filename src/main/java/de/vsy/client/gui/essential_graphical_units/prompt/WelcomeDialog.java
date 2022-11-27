/*
 *
 */
package de.vsy.client.gui.essential_graphical_units.prompt;

import static de.vsy.client.gui.essential_graphical_units.prompt.NavigationGoal.ACCOUNT_CREATION;
import static de.vsy.client.gui.essential_graphical_units.prompt.NavigationGoal.LOGIN;
import static java.lang.Short.MAX_VALUE;
import static javax.swing.GroupLayout.Alignment.LEADING;
import static javax.swing.GroupLayout.Alignment.TRAILING;
import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;
import static javax.swing.LayoutStyle.ComponentPlacement.UNRELATED;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serial;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;

public class WelcomeDialog extends JDialog {

  @Serial
  private static final long serialVersionUID = 4922916412599991228L;
  private JButton closeApplicationButton;
  private JButton welcomeLoginButton;
  private JButton welcomeNewAccButton;

  /**
   * Instantiates a new welcome dialog.
   *
   * @param ac the action listener
   */
  public WelcomeDialog(final ActionListener ac) {
    initComponents(ac);
  }

  private void initComponents(final ActionListener ac) {
    this.welcomeLoginButton = new JButton();
    this.welcomeNewAccButton = new JButton();
    this.closeApplicationButton = new JButton();
    var closeDialog = new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            WelcomeDialog.this.setVisible(false);
          }
        };

    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setTitle("Welcome");
    setAlwaysOnTop(true);
    setLocation(new Point(0, 0));
    setMinimumSize(new Dimension(260, 160));
    setModal(true);
    setSize(new Dimension(260, 160));
    setResizable(false);

    this.welcomeLoginButton.setText("Login");
    this.welcomeLoginButton.setMaximumSize(new Dimension(138, 25));
    this.welcomeLoginButton.setMinimumSize(new Dimension(138, 25));
    this.welcomeLoginButton.setPreferredSize(new Dimension(138, 25));
    this.welcomeLoginButton.setActionCommand(String.valueOf(LOGIN));
    this.welcomeLoginButton.addActionListener(ac);
    this.welcomeLoginButton.addActionListener(closeDialog);

    this.welcomeNewAccButton.setText("New Account");
    this.welcomeNewAccButton.setPreferredSize(new Dimension(138, 25));
    this.welcomeNewAccButton.setActionCommand(String.valueOf(ACCOUNT_CREATION));
    this.welcomeNewAccButton.addActionListener(ac);
    this.welcomeNewAccButton.addActionListener(closeDialog);

    this.closeApplicationButton.setText("Close Application");
    this.closeApplicationButton.setMaximumSize(new Dimension(138, 25));
    this.closeApplicationButton.setMinimumSize(new Dimension(138, 25));
    this.closeApplicationButton.setPreferredSize(new Dimension(138, 25));
    this.closeApplicationButton.setActionCommand("CLOSE_APPLICATION");
    this.closeApplicationButton.addActionListener(ac);
    this.closeApplicationButton.addActionListener(closeDialog);
    this.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        ac.actionPerformed(new ActionEvent(this, e.getID(), "CLOSE_APPLICATION"));
        dispose();
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
}
