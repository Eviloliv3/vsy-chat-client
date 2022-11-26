/*
 *
 */
package de.vsy.client.gui.essential_graphical_units.prompt;

import static de.vsy.shared_utility.standard_value.StandardStringProvider.STANDARD_EMPTY_STRING;
import static java.lang.Short.MAX_VALUE;
import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.LEADING;
import static javax.swing.GroupLayout.Alignment.TRAILING;
import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;
import static javax.swing.LayoutStyle.ComponentPlacement.RELATED;
import static javax.swing.LayoutStyle.ComponentPlacement.UNRELATED;

import java.awt.Dimension;
import java.io.Serial;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class AccountCreationPanel extends JPanel {

  @Serial
  private static final long serialVersionUID = 7076022514835506538L;

  private JTextField newAccFirstNameField;
  private JLabel newAccFirstNameLabel;
  private JTextField newAccLastNameField;
  private JLabel newAccLastNameLabel;
  private JTextField newAccUsernameField;
  private JLabel newAccountUsernameLabel;
  private JPasswordField newAccPasswordField;
  private JLabel newAccPasswordLabel;

  /**
   * Create the dialog.
   */
  public AccountCreationPanel() {
    initComponents();
  }

  private void initComponents() {

    this.newAccountUsernameLabel = new JLabel();
    this.newAccPasswordLabel = new JLabel();
    this.newAccountUsernameLabel = new JLabel();
    this.newAccUsernameField = new JTextField();
    this.newAccPasswordField = new JPasswordField();
    this.newAccLastNameLabel = new JLabel();
    this.newAccFirstNameLabel = new JLabel();
    this.newAccFirstNameField = new JTextField();
    this.newAccLastNameField = new JTextField();

    setPreferredSize(new Dimension(260, 265));
    setSize(new Dimension(260, 265));

    this.newAccountUsernameLabel.setText("Username:");

    this.newAccPasswordLabel.setText("Password:");

    this.newAccUsernameField.setMinimumSize(new Dimension(5, 20));

    this.newAccLastNameLabel.setText("Last name:");

    this.newAccFirstNameLabel.setText("First name:");

    this.newAccFirstNameField.setMinimumSize(new Dimension(5, 20));

    this.newAccLastNameField.setMinimumSize(new Dimension(5, 20));

    final var newAccountDialogLayout = new GroupLayout(this);
    setLayout(newAccountDialogLayout);
    newAccountDialogLayout.setHorizontalGroup(
        newAccountDialogLayout
            .createParallelGroup(LEADING)
            .addGroup(
                newAccountDialogLayout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addGroup(
                        newAccountDialogLayout
                            .createParallelGroup(LEADING)
                            .addGroup(
                                newAccountDialogLayout
                                    .createSequentialGroup()
                                    .addComponent(this.newAccFirstNameLabel)
                                    .addPreferredGap(RELATED, DEFAULT_SIZE, MAX_VALUE)
                                    .addComponent(
                                        this.newAccFirstNameField,
                                        PREFERRED_SIZE,
                                        125,
                                        PREFERRED_SIZE))
                            .addGroup(
                                TRAILING,
                                newAccountDialogLayout
                                    .createSequentialGroup()
                                    .addComponent(this.newAccLastNameLabel)
                                    .addPreferredGap(RELATED, DEFAULT_SIZE, MAX_VALUE)
                                    .addComponent(
                                        this.newAccLastNameField,
                                        PREFERRED_SIZE,
                                        125,
                                        PREFERRED_SIZE))
                            .addGroup(
                                TRAILING,
                                newAccountDialogLayout
                                    .createSequentialGroup()
                                    .addComponent(this.newAccountUsernameLabel))
                            .addGroup(
                                TRAILING,
                                newAccountDialogLayout
                                    .createSequentialGroup()
                                    .addComponent(this.newAccountUsernameLabel)
                                    .addPreferredGap(RELATED, 25, MAX_VALUE)
                                    .addComponent(
                                        this.newAccUsernameField,
                                        PREFERRED_SIZE,
                                        125,
                                        PREFERRED_SIZE))
                            .addGroup(
                                TRAILING,
                                newAccountDialogLayout
                                    .createSequentialGroup()
                                    .addComponent(this.newAccPasswordLabel)
                                    .addPreferredGap(RELATED, DEFAULT_SIZE, MAX_VALUE)
                                    .addComponent(
                                        this.newAccPasswordField,
                                        PREFERRED_SIZE,
                                        125,
                                        PREFERRED_SIZE)))
                    .addGap(20, 20, 20)));
    newAccountDialogLayout.setVerticalGroup(
        newAccountDialogLayout
            .createParallelGroup(LEADING)
            .addGroup(
                newAccountDialogLayout
                    .createSequentialGroup()
                    .addContainerGap(48, MAX_VALUE)
                    .addGroup(
                        newAccountDialogLayout
                            .createParallelGroup(BASELINE)
                            .addComponent(this.newAccFirstNameLabel)
                            .addComponent(
                                this.newAccFirstNameField,
                                PREFERRED_SIZE,
                                DEFAULT_SIZE,
                                PREFERRED_SIZE))
                    .addPreferredGap(UNRELATED)
                    .addGroup(
                        newAccountDialogLayout
                            .createParallelGroup(BASELINE)
                            .addComponent(this.newAccLastNameLabel)
                            .addComponent(
                                this.newAccLastNameField,
                                PREFERRED_SIZE,
                                DEFAULT_SIZE,
                                PREFERRED_SIZE))
                    .addPreferredGap(UNRELATED)
                    .addGroup(
                        newAccountDialogLayout
                            .createParallelGroup(BASELINE)
                            .addComponent(
                                this.newAccUsernameField,
                                PREFERRED_SIZE,
                                DEFAULT_SIZE,
                                PREFERRED_SIZE)
                            .addComponent(this.newAccountUsernameLabel))
                    .addPreferredGap(UNRELATED)
                    .addGroup(
                        newAccountDialogLayout
                            .createParallelGroup(BASELINE)
                            .addComponent(
                                this.newAccPasswordField,
                                PREFERRED_SIZE,
                                DEFAULT_SIZE,
                                PREFERRED_SIZE)
                            .addComponent(this.newAccPasswordLabel))
                    .addPreferredGap(UNRELATED)
                    .addGroup(
                        newAccountDialogLayout
                            .createParallelGroup(BASELINE)
                            .addComponent(this.newAccountUsernameLabel))
                    .addGap(15, 15, 15)));
    getAccessibleContext().setAccessibleName("accountCreationPanel");
  }

  public void clearInput() {
    this.newAccFirstNameField.setText(STANDARD_EMPTY_STRING);
    this.newAccLastNameField.setText(STANDARD_EMPTY_STRING);
    this.newAccUsernameField.setText(STANDARD_EMPTY_STRING);
    this.newAccPasswordField.setText(STANDARD_EMPTY_STRING);


  }

  /**
   * Gets the first name.
   *
   * @return the first name
   */
  public String getFirstName() {
    return this.newAccFirstNameField.getText();
  }

  /**
   * Gets the last name.
   *
   * @return the last name
   */
  public String getLastName() {
    return this.newAccLastNameField.getText();
  }

  /**
   * Gets the login name.
   *
   * @return the login name
   */
  public String getLogin() {
    return this.newAccUsernameField.getText();
  }

  /**
   * Gets the password.
   *
   * @return the password
   */
  public char[] getPassword() {
    return this.newAccPasswordField.getPassword();
  }
}
