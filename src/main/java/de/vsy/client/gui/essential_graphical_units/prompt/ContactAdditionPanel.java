/*
 *
 */
package de.vsy.client.gui.essential_graphical_units.prompt;

import static java.lang.Short.MAX_VALUE;
import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.LEADING;
import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;

import java.awt.Dimension;
import java.io.Serial;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public
class ContactAdditionPanel extends JPanel {

  @Serial
  private static final long serialVersionUID = -7039554282835949881L;
  private JTextField newContactIdField;

  /**
   * Create the dialog.
   */
  public ContactAdditionPanel() {
    initComponents();
  }

  private void initComponents() {
    var addContactIdLabel = new JLabel();
    this.newContactIdField = new JTextField();

    setMaximumSize(new Dimension(260, 160));
    setPreferredSize(new Dimension(260, 160));
    setMinimumSize(new Dimension(260, 160));
    addContactIdLabel.setText("Contact id:");

    this.newContactIdField = new JTextField();
    this.newContactIdField.setPreferredSize(new Dimension(120, 19));
    this.newContactIdField.setMaximumSize(new Dimension(120, 19));
    this.newContactIdField.setMinimumSize(new Dimension(120, 19));
    this.newContactIdField.setSize(new Dimension(120, 19));
    this.newContactIdField.setColumns(10);
    getAccessibleContext().setAccessibleName("contactAdditionPanel");
    final var groupLayout = new GroupLayout(this);
    setLayout(groupLayout);
    groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(LEADING)
        .addGroup(groupLayout.createSequentialGroup()
            .addGap(20)
            .addGroup(groupLayout.createParallelGroup(LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addComponent(
                        addContactIdLabel)
                    .addGap(37)
                    .addComponent(this.newContactIdField, PREFERRED_SIZE, DEFAULT_SIZE,
                        PREFERRED_SIZE)))
            .addContainerGap(18, MAX_VALUE)));
    groupLayout.setVerticalGroup(groupLayout.createParallelGroup(LEADING)
        .addGroup(groupLayout.createSequentialGroup()
            .addGap(40)
            .addGroup(groupLayout.createParallelGroup(BASELINE)
                .addComponent(
                    addContactIdLabel)
                .addComponent(this.newContactIdField, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
            .addContainerGap()));
  }

  /**
   * Gets the contact id.
   *
   * @return the contact id
   */
  public int getContactId() {
    return Integer.parseInt(this.newContactIdField.getText());
  }
}
