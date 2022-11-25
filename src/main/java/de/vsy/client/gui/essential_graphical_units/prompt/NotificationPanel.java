/*
 *
 */
package de.vsy.client.gui.essential_graphical_units.prompt;

import static java.lang.Short.MAX_VALUE;
import static javax.swing.GroupLayout.Alignment.LEADING;
import static javax.swing.GroupLayout.Alignment.TRAILING;
import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;

import java.awt.Dimension;
import java.io.Serial;
import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public
class NotificationPanel extends JPanel {
  @Serial
  private static final long serialVersionUID = 6289227728904151492L;
  private final String notification;


  /**
   * Create the dialog.
   *
   * @param notification the message
   */
  public NotificationPanel(final String notification) {
    this.notification = notification;
    initComponents();
  }

  private void initComponents() {
    final var statusMessageField = new JTextArea(notification);

    setMaximumSize(new Dimension(260, 160));
    setMinimumSize(new Dimension(260, 160));
    setPreferredSize(new Dimension(260, 160));

    statusMessageField.setLineWrap(true);
    statusMessageField.setEditable(false);

    final var statusDialogLayout = new GroupLayout(this);
    setLayout(statusDialogLayout);
    statusDialogLayout.setHorizontalGroup(statusDialogLayout.createParallelGroup(LEADING)
        .addGroup(statusDialogLayout.createSequentialGroup()
            .addGap(18)
            .addComponent(statusMessageField, PREFERRED_SIZE,
                208, PREFERRED_SIZE)
            .addContainerGap(18, MAX_VALUE))
        .addGroup(TRAILING,
            statusDialogLayout.createSequentialGroup()
                .addGap(53)));
    statusDialogLayout.setVerticalGroup(statusDialogLayout.createParallelGroup(LEADING)
        .addGroup(statusDialogLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(statusMessageField, PREFERRED_SIZE,
                73, PREFERRED_SIZE)
            .addContainerGap(DEFAULT_SIZE, MAX_VALUE)));
  }
}
