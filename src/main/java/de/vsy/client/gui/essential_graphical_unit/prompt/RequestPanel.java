package de.vsy.client.gui.essential_graphical_unit.prompt;

import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * @author fredward
 */
public class RequestPanel extends JPanel {

  /**
   * Creates new form LoginPanel
   */
  public RequestPanel(final String message) {
    initComponents(message);
  }

  private void initComponents(final String message) {

    JScrollPane jScrollPane1 = new JScrollPane();
    JTextArea requestField = new JTextArea(message);

    requestField.setEditable(false);
    requestField.setColumns(20);
    requestField.setLineWrap(true);
    requestField.setRows(2);
    requestField.setWrapStyleWord(true);
    jScrollPane1.setViewportView(requestField);

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
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .addContainerGap())
    );

    getAccessibleContext().setAccessibleName("");
    getAccessibleContext().setAccessibleDescription("");
  }
}
