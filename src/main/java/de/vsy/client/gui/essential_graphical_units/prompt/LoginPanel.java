/*
 *
 */
package de.vsy.client.gui.essential_graphical_units.prompt;

import static java.lang.Short.MAX_VALUE;
import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.LEADING;
import static javax.swing.GroupLayout.Alignment.TRAILING;
import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;
import static javax.swing.LayoutStyle.ComponentPlacement.RELATED;
import static javax.swing.LayoutStyle.ComponentPlacement.UNRELATED;

import java.awt.Dimension;
import java.awt.Point;
import java.io.Serial;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginPanel extends JPanel {

  @Serial
  private static final long serialVersionUID = -290337388982445470L;

  private JTextField usernameField;
  private JPasswordField loginPasswordField;

  /**
   * Create the dialog.
   */
  public LoginPanel() {
    initComponents();
  }

  private void initComponents() {
    JLabel usernameLabel = new JLabel();
    JLabel loginPasswordLabel = new JLabel();

    this.usernameField = new JTextField();
    this.loginPasswordField = new JPasswordField();

    setLocation(new Point(75, 100));
    setMinimumSize(new Dimension(260, 160));
    setPreferredSize(new Dimension(260, 160));
    setSize(new Dimension(260, 160));

    usernameLabel.setText("Username");

    loginPasswordLabel.setText("Password");

    this.usernameField.setPreferredSize(new Dimension(120, 19));

    this.loginPasswordField.setPreferredSize(new Dimension(120, 19));

    final var loginDialogLayout = new GroupLayout(this);
    setLayout(loginDialogLayout);
    loginDialogLayout.setHorizontalGroup(
        loginDialogLayout
            .createParallelGroup(LEADING)
            .addGroup(
                loginDialogLayout
                    .createSequentialGroup()
                    .addGroup(
                        loginDialogLayout
                            .createParallelGroup(LEADING)
                            .addGroup(
                                loginDialogLayout
                                    .createSequentialGroup()
                                    .addGap(19, 19, 19)
                                    .addGroup(
                                        loginDialogLayout
                                            .createParallelGroup(LEADING)
                                            .addComponent(usernameLabel)
                                            .addComponent(loginPasswordLabel))
                                    .addPreferredGap(RELATED)
                                    .addGroup(
                                        loginDialogLayout
                                            .createParallelGroup(LEADING)
                                            .addComponent(
                                                this.usernameField,
                                                PREFERRED_SIZE,
                                                120,
                                                PREFERRED_SIZE)
                                            .addComponent(
                                                this.loginPasswordField,
                                                PREFERRED_SIZE,
                                                120,
                                                PREFERRED_SIZE))))
                    .addContainerGap(18, MAX_VALUE)));
    loginDialogLayout.setVerticalGroup(
        loginDialogLayout
            .createParallelGroup(LEADING)
            .addGroup(
                loginDialogLayout
                    .createSequentialGroup()
                    .addGap(15, 15, 15)
                    .addGroup(
                        loginDialogLayout
                            .createParallelGroup(BASELINE)
                            .addComponent(usernameLabel)
                            .addComponent(
                                this.usernameField, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
                    .addPreferredGap(UNRELATED)
                    .addGroup(
                        loginDialogLayout
                            .createParallelGroup(TRAILING)
                            .addComponent(loginPasswordLabel)
                            .addComponent(
                                this.loginPasswordField,
                                PREFERRED_SIZE,
                                DEFAULT_SIZE,
                                PREFERRED_SIZE)))
            .addGap(15, 15, 15));

    getAccessibleContext().setAccessibleName("loginPanel");
  }

  /**
   * Gets the login name.
   *
   * @return the login name
   */
  public String getUsername() {
    return this.usernameField.getText();
  }

  /**
   * Gets the password.
   *
   * @return the password
   */
  public char[] getPassword() {
    return this.loginPasswordField.getPassword();
  }
}
