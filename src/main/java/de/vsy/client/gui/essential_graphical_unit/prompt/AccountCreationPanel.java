package de.vsy.client.gui.essential_graphical_unit.prompt;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;

/**
 * @author fredward
 */
public class AccountCreationPanel extends JPanel {

  private JTextField firstNameField;
  private JTextField lastNameField;
  private JPasswordField passwordField;
  private JTextField usernameField;

  public AccountCreationPanel() {
    initComponents();
  }

  private void initComponents() {

    JLabel firstNameLabel = new JLabel();
    JLabel lastNameLabel = new JLabel();
    JLabel lastNameLabel1 = new JLabel();
    JLabel lastNameLabel2 = new JLabel();
    this.passwordField = new JPasswordField();
    this.firstNameField = new JTextField();
    this.lastNameField = new JTextField();
    this.usernameField = new JTextField();

    firstNameLabel.setHorizontalAlignment(SwingConstants.TRAILING);
    firstNameLabel.setLabelFor(this.firstNameField);
    firstNameLabel.setText("First name:");
    firstNameLabel.setName("firstNameLabel");

    this.passwordField.setColumns(15);
    this.firstNameField.setColumns(15);
    this.lastNameField.setColumns(15);
    this.usernameField.setColumns(15);

    lastNameLabel.setHorizontalAlignment(SwingConstants.TRAILING);
    lastNameLabel.setLabelFor(this.firstNameField);
    lastNameLabel.setText("Last name:");
    lastNameLabel.setName("firstNameLabel");

    lastNameLabel1.setHorizontalAlignment(SwingConstants.TRAILING);
    lastNameLabel1.setLabelFor(this.firstNameField);
    lastNameLabel1.setText("Username:");
    lastNameLabel1.setName("firstNameLabel");

    lastNameLabel2.setHorizontalAlignment(SwingConstants.TRAILING);
    lastNameLabel2.setLabelFor(this.firstNameField);
    lastNameLabel2.setText("Password:");
    lastNameLabel2.setName("firstNameLabel");

    GroupLayout layout = new GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(firstNameLabel, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lastNameLabel, GroupLayout.Alignment.LEADING,
                        GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lastNameLabel1, GroupLayout.Alignment.LEADING,
                        GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lastNameLabel2, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(this.lastNameField, GroupLayout.PREFERRED_SIZE, 1,
                        Short.MAX_VALUE)
                    .addComponent(this.firstNameField, GroupLayout.DEFAULT_SIZE, 108,
                        Short.MAX_VALUE)
                    .addComponent(this.usernameField, GroupLayout.PREFERRED_SIZE, 1,
                        Short.MAX_VALUE)
                    .addComponent(this.passwordField, GroupLayout.Alignment.TRAILING,
                        GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                .addContainerGap())
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(firstNameLabel)
                    .addComponent(this.firstNameField, GroupLayout.PREFERRED_SIZE,
                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(this.lastNameField, GroupLayout.PREFERRED_SIZE, 25,
                        GroupLayout.PREFERRED_SIZE)
                    .addComponent(lastNameLabel))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(this.usernameField, GroupLayout.PREFERRED_SIZE, 25,
                        GroupLayout.PREFERRED_SIZE)
                    .addComponent(lastNameLabel1))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(this.passwordField, GroupLayout.PREFERRED_SIZE, 25,
                        GroupLayout.PREFERRED_SIZE)
                    .addComponent(lastNameLabel2))
                .addContainerGap())
    );
  }

  public String getFirstName() {
    return this.firstNameField.getText();
  }

  public String getLastName() {
    return this.lastNameField.getText();
  }

  public String getUsername() {
    return this.usernameField.getText();
  }

  public char[] getPassword() {
    return this.passwordField.getPassword();
  }
}
