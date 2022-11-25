/*
 *
 */
package de.vsy.client.gui.essential_graphical_units.prompt;

import static java.lang.Short.MAX_VALUE;
import static javax.swing.GroupLayout.Alignment.LEADING;
import static javax.swing.GroupLayout.PREFERRED_SIZE;
import static javax.swing.LayoutStyle.ComponentPlacement.RELATED;

import java.awt.Dimension;
import java.io.Serial;
import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class RequestPanel extends JPanel {

  @Serial
  private static final long serialVersionUID = -3450550022346056050L;
  private JTextArea requestMessageField;

  /**
   * Create the dialog.
   * @param request          the request
   */
  public RequestPanel(final String request) {
    initComponents(request);
  }

  public final void initComponents(final String request) {
    this.requestMessageField = new JTextArea(request);
    setMaximumSize(new Dimension(260, 160));
    setMinimumSize(new Dimension(260, 160));
    setPreferredSize(new Dimension(260, 160));

    this.requestMessageField.setLineWrap(true);
    this.requestMessageField.setEditable(false);

    final var statusDialogLayout = new GroupLayout(this);
    setLayout(statusDialogLayout);
    statusDialogLayout.setHorizontalGroup(
        statusDialogLayout
            .createParallelGroup(LEADING)
            .addGroup(
                statusDialogLayout
                    .createSequentialGroup()
                    .addGroup(
                        statusDialogLayout
                            .createParallelGroup(LEADING)
                            .addGroup(
                                statusDialogLayout
                                    .createSequentialGroup()
                                    .addGap(18)
                                    .addComponent(
                                        this.requestMessageField,
                                        PREFERRED_SIZE,
                                        208,
                                        PREFERRED_SIZE)))
                    .addContainerGap(24, MAX_VALUE)));
    statusDialogLayout.setVerticalGroup(
        statusDialogLayout
            .createParallelGroup(LEADING)
            .addGroup(
                statusDialogLayout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addComponent(this.requestMessageField, PREFERRED_SIZE, 73, PREFERRED_SIZE)
                    .addPreferredGap(RELATED)
                    .addGap(15)));
  }
}
