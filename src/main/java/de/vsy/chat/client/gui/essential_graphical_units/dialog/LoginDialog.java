/*
 *
 */
package de.vsy.chat.client.gui.essential_graphical_units.dialog;

import static de.vsy.chat.client.gui.essential_graphical_units.dialog.DialogStatus.INITIAL;
import static de.vsy.chat.client.gui.essential_graphical_units.dialog.DialogType.INITIALIZATION;
import static de.vsy.chat.utility.standard_value.StandardIdProvider.STANDARD_SERVER_ID;

import de.vsy.chat.transmission.packet.content.PacketContent;
import de.vsy.chat.transmission.packet.content.authentication.LoginRequestDTO;
import de.vsy.chat.transmission.packet.content.error.ErrorDTO;
import de.vsy.chat.client.controlling.essential_gui_action_interfaces.DialogEssentialActions;
import de.vsy.chat.client.controlling.essential_gui_action_interfaces.DialogInitNavigation;
import de.vsy.chat.module.security.password.PasswordHasher;

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

public class LoginDialog extends ChatClientDialog {

  private static final long serialVersionUID = -290337388982445470L;
  private static final PasswordHasher PASSWORD_HASHER;
  private final DialogEssentialActions actionControl;
  private final DialogInitNavigation navigator;
  private JButton loginButton;
  private JButton loginCancelButton;
  private JTextField loginNameField;
  private JLabel loginNameLabel;
  private JPasswordField loginPasswordField;
  private JLabel loginPasswordLabel;

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
  public LoginDialog(
      final DialogEssentialActions actionController, final DialogInitNavigation navigator) {
    super(INITIALIZATION);
    this.actionControl = actionController;
    this.navigator = navigator;
    initComponents();
  }

  private void initComponents() {
    this.loginNameLabel = new JLabel();
    this.loginPasswordLabel = new JLabel();

    this.loginNameField = new JTextField();
    this.loginPasswordField = new JPasswordField();

    this.loginButton = new JButton();
    this.loginCancelButton = new JButton();

    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setTitle("Eingabe von Zugangsinformationen");
    setAlwaysOnTop(true);
    setLocation(new Point(75, 100));
    setMinimumSize(new Dimension(260, 160));
    setModal(true);
    setPreferredSize(new Dimension(260, 160));
    setSize(new Dimension(260, 160));
    setResizable(false);

    this.loginNameLabel.setText("Login-Name");

    this.loginPasswordLabel.setText("Passwort");

    this.loginNameField.setPreferredSize(new Dimension(120, 19));

    this.loginPasswordField.setPreferredSize(new Dimension(120, 19));

    this.loginButton.setText("Einloggen");
    this.loginButton.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(final MouseEvent evt) {
            confirmation();
          }
        });

    this.loginCancelButton.setText("Abbrechen");
    this.loginCancelButton.addMouseListener(
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
            actionControl.closeDialog(getDialogType());
          }
        });

    final var loginDialogLayout = new GroupLayout(getContentPane());
    getContentPane().setLayout(loginDialogLayout);
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
                                            .addComponent(this.loginNameLabel)
                                            .addComponent(this.loginPasswordLabel))
                                    .addPreferredGap(RELATED)
                                    .addGroup(
                                        loginDialogLayout
                                            .createParallelGroup(LEADING)
                                            .addComponent(
                                                this.loginNameField,
                                                PREFERRED_SIZE,
                                                120,
                                                PREFERRED_SIZE)
                                            .addComponent(
                                                this.loginPasswordField,
                                                PREFERRED_SIZE,
                                                120,
                                                PREFERRED_SIZE)))
                            .addGroup(
                                loginDialogLayout
                                    .createSequentialGroup()
                                    .addGap(16, 16, 16)
                                    .addComponent(this.loginButton)
                                    .addPreferredGap(UNRELATED)
                                    .addComponent(this.loginCancelButton)))
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
                            .addComponent(this.loginNameLabel)
                            .addComponent(
                                this.loginNameField, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
                    .addPreferredGap(UNRELATED)
                    .addGroup(
                        loginDialogLayout
                            .createParallelGroup(TRAILING)
                            .addComponent(this.loginPasswordLabel)
                            .addComponent(
                                this.loginPasswordField,
                                PREFERRED_SIZE,
                                DEFAULT_SIZE,
                                PREFERRED_SIZE))
                    .addPreferredGap(RELATED, 55, MAX_VALUE)
                    .addGroup(
                        loginDialogLayout
                            .createParallelGroup(BASELINE)
                            .addComponent(this.loginButton)
                            .addComponent(this.loginCancelButton))
                    .addGap(15, 15, 15)));

    getAccessibleContext().setAccessibleName("_loginDialog");
  }

  private void confirmation() {
    this.actionControl.evaluateDialog(getDialogType(), STANDARD_SERVER_ID);
  }

  private void cancellation() {
    this.navigator.navigate(INITIAL);
  }

  @Override
  public PacketContent evaluate() {
    PacketContent data = null;
    String loginName = null;
    char[] passwordChars;

    loginName = getLogin();
    passwordChars = getPassword();

    if (loginName.length() < 5) {
      data =
          new ErrorDTO(
              "Die eingegebenen Daten wurden nicht uebertragen."
                  + "Der Loginname hat nicht die minimal erforderliche Laenge.",
              null);
    } else if (passwordChars.length < 5) {
      data =
          new ErrorDTO(
              "Die eingegebenen Daten wurden nicht uebertragen."
                  + "Das Passwort hat nicht die minimal erforderliche Laenge.",
              null);
    } else {
      var hashedPassword = PASSWORD_HASHER.calculateHash(getPassword());
      data = new LoginRequestDTO(loginName, hashedPassword);
    }
    return data;
  }

  /**
   * Gets the login name.
   *
   * @return the login name
   */
  public String getLogin() {
    return this.loginNameField.getText();
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
