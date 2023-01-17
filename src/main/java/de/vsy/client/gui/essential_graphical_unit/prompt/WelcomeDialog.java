package de.vsy.client.gui.essential_graphical_unit.prompt;

import static de.vsy.client.gui.essential_graphical_unit.NavigationGoal.ACCOUNT_CREATION;
import static de.vsy.client.gui.essential_graphical_unit.NavigationGoal.CLOSE_APPLICATION;
import static de.vsy.client.gui.essential_graphical_unit.NavigationGoal.LOGIN;
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serial;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;

public class WelcomeDialog extends JDialog {

  @Serial
  private static final long serialVersionUID = 4922916412599991228L;

  /**
   * Instantiates a new welcome dialog.
   *
   * @param listener the action listener
   */
  public WelcomeDialog(final ActionListener listener) {
    initComponents(listener);
  }

  private void initComponents(final ActionListener listener) {
    JButton welcomeLoginButton = new JButton();
    JButton welcomeNewAccButton = new JButton();
    JButton closeApplicationButton = new JButton();

    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setTitle("Welcome");
    setAlwaysOnTop(true);
    setLocation(new Point(0, 0));
    setMinimumSize(new Dimension(260, 160));
    setModal(true);
    setSize(new Dimension(260, 160));
    setResizable(false);

    welcomeLoginButton.setText("Login");
    welcomeLoginButton.setMaximumSize(new Dimension(138, 25));
    welcomeLoginButton.setMinimumSize(new Dimension(138, 25));
    welcomeLoginButton.setPreferredSize(new Dimension(138, 25));
    welcomeLoginButton.setActionCommand(LOGIN.toString());
    welcomeLoginButton.addActionListener(e -> {
      WelcomeDialog.this.dispose();
      listener.actionPerformed(e);
    });

    welcomeNewAccButton.setText("New Account");
    welcomeNewAccButton.setPreferredSize(new Dimension(138, 25));
    welcomeNewAccButton.setActionCommand(ACCOUNT_CREATION.toString());
    welcomeNewAccButton.addActionListener(e -> {
      WelcomeDialog.this.dispose();
      listener.actionPerformed(e);
    });

    closeApplicationButton.setText("Close Application");
    closeApplicationButton.setMaximumSize(new Dimension(138, 25));
    closeApplicationButton.setMinimumSize(new Dimension(138, 25));
    closeApplicationButton.setPreferredSize(new Dimension(138, 25));
    closeApplicationButton.setActionCommand(CLOSE_APPLICATION.toString());
    closeApplicationButton.addActionListener(e -> {
      WelcomeDialog.this.dispose();
      listener.actionPerformed(e);
    });
    this.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        dispose();
        listener.actionPerformed(
            new ActionEvent(this, e.getID(), CLOSE_APPLICATION.toString()));
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
                                closeApplicationButton, PREFERRED_SIZE, 138, PREFERRED_SIZE)
                            .addComponent(
                                welcomeNewAccButton, PREFERRED_SIZE, 138, PREFERRED_SIZE)
                            .addComponent(
                                welcomeLoginButton, PREFERRED_SIZE, 138, PREFERRED_SIZE))
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
                        welcomeLoginButton, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                    .addPreferredGap(UNRELATED)
                    .addComponent(
                        welcomeNewAccButton, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(
                        closeApplicationButton, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                    .addContainerGap(40, MAX_VALUE)));
  }
}
