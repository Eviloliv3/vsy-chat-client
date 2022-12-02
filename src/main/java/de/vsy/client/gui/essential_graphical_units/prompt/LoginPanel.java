package de.vsy.client.gui.essential_graphical_units.prompt;

import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author fredward
 */
public class LoginPanel extends javax.swing.JPanel {


  private JPasswordField passwordField;
  private JTextField usernameField;

  public LoginPanel() {
    initComponents();
  }
  private void initComponents() {

    JLabel usernameLabel = new JLabel();
    JLabel passwordLabel = new JLabel();
    usernameField = new JTextField();
    passwordField = new JPasswordField();

    usernameLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
    usernameLabel.setLabelFor(usernameField);
    usernameLabel.setText("Username:");
    usernameLabel.setName("usernameLabel"); // NOI18N

    passwordLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
    passwordLabel.setLabelFor(passwordField);
    passwordLabel.setText("Password:");

    usernameField.setColumns(15);
    passwordField.setColumns(15);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(usernameLabel)
                    .addComponent(passwordLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(usernameField, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                .addContainerGap())
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(usernameLabel))
                    .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordLabel)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
    );
  }

  public String getUsername (){
    return this.usernameField.getText();
  }

  public char[] getPassword(){
    return this.passwordField.getPassword();
  }
}
