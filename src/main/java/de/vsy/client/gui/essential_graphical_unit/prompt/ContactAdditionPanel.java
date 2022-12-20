/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package de.vsy.client.gui.essential_graphical_unit.prompt;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;

/**
 * @author fredward
 */
public class ContactAdditionPanel extends JPanel {

  private JTextField contactIdField;

  public ContactAdditionPanel() {
    initComponents();
  }

  private void initComponents() {

    JLabel contactIdLabel = new JLabel();
    this.contactIdField = new JTextField();

    contactIdLabel.setHorizontalAlignment(SwingConstants.TRAILING);
    contactIdLabel.setLabelFor(this.contactIdField);
    contactIdLabel.setText("Contact ID:");
    this.contactIdField.setColumns(15);

    GroupLayout layout = new GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(contactIdLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
                    Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(this.contactIdField, GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
                .addContainerGap())
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(contactIdLabel)
                    .addComponent(this.contactIdField, GroupLayout.PREFERRED_SIZE,
                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
  }

  public int getContactId() throws NumberFormatException {
    return Integer.parseInt(this.contactIdField.getText());
  }
}
