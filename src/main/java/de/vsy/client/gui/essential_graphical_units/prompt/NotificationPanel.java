/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package de.vsy.client.gui.essential_graphical_units.prompt;

import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author fredward
 */
public class NotificationPanel extends JPanel {

  public NotificationPanel(final String message) {
    initComponents(message);
  }

  private void initComponents(final String message) {

    JScrollPane jScrollPane1 = new JScrollPane();
    JTextArea notificationField = new JTextArea(message);

    notificationField.setEditable(false);
    notificationField.setColumns(20);
    notificationField.setLineWrap(true);
    notificationField.setRows(5);
    notificationField.setText("Friendship request was no processed. You already are friends with");
    notificationField.setWrapStyleWord(true);
    jScrollPane1.setViewportView(notificationField);

    GroupLayout layout = new GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                .addContainerGap())
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                .addContainerGap())
    );

    getAccessibleContext().setAccessibleName("");
    getAccessibleContext().setAccessibleDescription("");
  }
}
