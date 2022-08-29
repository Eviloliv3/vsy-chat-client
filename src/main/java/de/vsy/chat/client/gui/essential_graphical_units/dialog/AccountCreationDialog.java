/*
 *
 */
package de.vsy.chat.client.gui.essential_graphical_units.dialog;

import static de.vsy.chat.client.gui.essential_graphical_units.dialog.DialogStatus.INITIAL;
import static de.vsy.chat.client.gui.essential_graphical_units.dialog.DialogType.INITIALIZATION;
import static de.vsy.chat.utility.standard_value.StandardIdProvider.STANDARD_SERVER_ID;

import de.vsy.chat.client.controlling.essential_gui_action_interfaces.DialogEssentialActions;
import de.vsy.chat.client.controlling.essential_gui_action_interfaces.DialogInitNavigation;
import de.vsy.chat.module.security.password.PasswordHasher;
import de.vsy.chat.transmission.dto.authentication.AccountCreationDTO;
import de.vsy.chat.transmission.dto.authentication.AuthenticationDTO;
import de.vsy.chat.transmission.dto.authentication.PersonalData;
import de.vsy.chat.transmission.packet.content.PacketContent;
import de.vsy.chat.transmission.packet.content.authentication.NewAccountRequestDTO;
import de.vsy.chat.transmission.packet.content.error.ErrorDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import static java.lang.Short.MAX_VALUE;
import static java.security.MessageDigest.getInstance;
import java.security.NoSuchAlgorithmException;
import java.util.ServiceConfigurationError;
import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.LEADING;
import static javax.swing.GroupLayout.Alignment.TRAILING;
import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;
import static javax.swing.LayoutStyle.ComponentPlacement.RELATED;
import static javax.swing.LayoutStyle.ComponentPlacement.UNRELATED;

public class AccountCreationDialog extends ChatClientDialog {

  private static final long serialVersionUID = 7076022514835506538L;
  private static final PasswordHasher PASSWORD_HASHER;
  private final DialogEssentialActions actionControl;
  private final DialogInitNavigation navigator;
  private JButton newAccCancelButton;
  private JTextField newAccFirstNameField;
  private JLabel newAccFirstNameLabel;
  private JTextField newAccLastNameField;
  private JLabel newAccLastNameLabel;
  private JTextField newAccLoginNameField;
  private JLabel newAccountLoginNameLabel;
  private JPasswordField newAccPasswordField;
  private JLabel newAccPasswordLabel;
  private JPasswordField newAccPasswordRepeatField;
  private JLabel newAccPasswordRepeatLabel;
  private JButton newAccSaveButton;

  static {
    try {
      PASSWORD_HASHER = new PasswordHasher(getInstance("SHASTANDARD_SERVER_ID"));
    } catch (NoSuchAlgorithmException e) {
      throw new ServiceConfigurationError(
          e.getLocalizedMessage()
              + "\nKein SHASTANDARD_SERVER_ID MessageDigest Objekt "
              + "gefunden.");
    }
  }

  /**
   * Create the dialog.
   *
   * @param actionController the action controller
   * @param navigator the navigator
   */
  public AccountCreationDialog(
      final DialogEssentialActions actionController, final DialogInitNavigation navigator) {
    super(INITIALIZATION);
    this.actionControl = actionController;
    this.navigator = navigator;
    initComponents();
  }

  private void initComponents() {

    this.newAccountLoginNameLabel = new JLabel();
    this.newAccPasswordLabel = new JLabel();
    this.newAccPasswordRepeatLabel = new JLabel();
    this.newAccountLoginNameLabel = new JLabel();
    this.newAccLoginNameField = new JTextField();
    this.newAccPasswordField = new JPasswordField();
    this.newAccPasswordRepeatField = new JPasswordField();
    this.newAccSaveButton = new JButton();
    this.newAccCancelButton = new JButton();
    this.newAccLastNameLabel = new JLabel();
    this.newAccFirstNameLabel = new JLabel();
    this.newAccFirstNameField = new JTextField();
    this.newAccLastNameField = new JTextField();

    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setTitle("Eingabe von Zugangsinformationen");
    setAlwaysOnTop(true);
    setModal(true);
    setResizable(false);
    setPreferredSize(new Dimension(260, 265));
    setSize(new Dimension(260, 265));

    this.newAccountLoginNameLabel.setText("Login-Name");

    this.newAccPasswordLabel.setText("Passwort");

    this.newAccPasswordRepeatLabel.setText("Passwort");

    this.newAccLoginNameField.setMinimumSize(new Dimension(5, 20));

    this.newAccSaveButton.setText("Speichern");
    this.newAccSaveButton.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(final MouseEvent evt) {
            confirmation();
          }
        });

    this.newAccCancelButton.setText("Abbrechen");
    this.newAccCancelButton.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(final MouseEvent evt) {
            cancellation();
          }
        });

    addWindowListener(
        new WindowAdapter() {
          @Override
          public void windowClosed(final WindowEvent evt) {
            cancellation();
          }
        });

    this.newAccLastNameLabel.setText("Nachname");

    this.newAccFirstNameLabel.setText("Vorname");

    this.newAccFirstNameField.setMinimumSize(new Dimension(5, 20));

    this.newAccLastNameField.setMinimumSize(new Dimension(5, 20));

    final var newAccountDialogLayout = new GroupLayout(getContentPane());
    getContentPane().setLayout(newAccountDialogLayout);
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
                                    .addComponent(this.newAccountLoginNameLabel)
                                    .addPreferredGap(RELATED, DEFAULT_SIZE, MAX_VALUE)
                                    .addComponent(
                                        this.newAccPasswordRepeatField,
                                        PREFERRED_SIZE,
                                        125,
                                        PREFERRED_SIZE))
                            .addGroup(
                                TRAILING,
                                newAccountDialogLayout
                                    .createSequentialGroup()
                                    .addComponent(this.newAccountLoginNameLabel)
                                    .addPreferredGap(RELATED, 25, MAX_VALUE)
                                    .addComponent(
                                        this.newAccLoginNameField,
                                        PREFERRED_SIZE,
                                        125,
                                        PREFERRED_SIZE))
                            .addGroup(
                                TRAILING,
                                newAccountDialogLayout
                                    .createSequentialGroup()
                                    .addGap(0, 0, MAX_VALUE)
                                    .addComponent(this.newAccSaveButton)
                                    .addGap(5, 5, 5)
                                    .addComponent(this.newAccCancelButton))
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
                                this.newAccLoginNameField,
                                PREFERRED_SIZE,
                                DEFAULT_SIZE,
                                PREFERRED_SIZE)
                            .addComponent(this.newAccountLoginNameLabel))
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
                            .addComponent(this.newAccountLoginNameLabel)
                            .addComponent(
                                this.newAccPasswordRepeatField,
                                PREFERRED_SIZE,
                                DEFAULT_SIZE,
                                PREFERRED_SIZE))
                    .addPreferredGap(UNRELATED)
                    .addGroup(
                        newAccountDialogLayout
                            .createParallelGroup(BASELINE)
                            .addComponent(this.newAccSaveButton)
                            .addComponent(this.newAccCancelButton))
                    .addGap(15, 15, 15)));
    getAccessibleContext().setAccessibleName("_newAccountDialog");
  }

  private void confirmation() {
    this.actionControl.evaluateDialog(getDialogType(), STANDARD_SERVER_ID);
  }

  private void cancellation() {
    this.actionControl.closeDialog(getDialogType());
    this.navigator.navigate(INITIAL);
  }

  @Override
  public PacketContent evaluate() {
    PacketContent data = null;
    var firstName = getFirstName();
    var lastName = getLastName();
    var loginName = getLogin();
    var password = PASSWORD_HASHER.calculateHash(getPassword());
    var passwordRepetition = PASSWORD_HASHER.calculateHash(getRepeatedPassword());

    if (password.equals(passwordRepetition)) {
      data =
          new NewAccountRequestDTO(
              new AccountCreationDTO(
                  AuthenticationDTO.valueOf(loginName, password),
                  PersonalData.valueOf(firstName, lastName)));
    } else {
      data =
          new ErrorDTO(
              "Die Account-Daten wurden nicht uebertragen."
                  + "Die eingegebenen Passwoerter stimmen nicht ueberein.",
              null);
    }

    return data;
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
    return this.newAccLoginNameField.getText();
  }

  /**
   * Gets the password.
   *
   * @return the password
   */
  public char[] getPassword() {
    return this.newAccPasswordField.getPassword();
  }

  /**
   * Gets the repeated password.
   *
   * @return the repeated password
   */
  public char[] getRepeatedPassword() {
    return this.newAccPasswordRepeatField.getPassword();
  }
}
