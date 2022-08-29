/*
 *
 */
package de.vsy.chat.client.gui.essential_graphical_units.dialog;

import de.vsy.chat.transmission.packet.content.HumanInteractionRequest;
import de.vsy.chat.transmission.packet.content.PacketContent;
import de.vsy.chat.client.controlling.essential_gui_action_interfaces.DialogEssentialActions;
import de.vsy.chat.module.packet_content_translation.HumanInteractionRequestTranslator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static de.vsy.chat.client.gui.essential_graphical_units.dialog.DialogType.REQUEST;
import static java.lang.Short.MAX_VALUE;
import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.LEADING;
import static javax.swing.GroupLayout.PREFERRED_SIZE;
import static javax.swing.LayoutStyle.ComponentPlacement.RELATED;

public class RequestDialog extends ChatClientDialog {

  private static final long serialVersionUID = -3450550022346056050L;
  private final DialogEssentialActions actionControl;
  private boolean decision;
  private HumanInteractionRequest request;
  private JTextArea requestMessageField;

  /**
   * Instantiates a new request dialog.
   *
   * @param actionController the action controller
   */
  public RequestDialog(final DialogEssentialActions actionController) {
    this(actionController, null);
  }

  /**
   * Create the dialog.
   *
   * @param actionController the action controller
   * @param request the request
   */
  public RequestDialog(
      final DialogEssentialActions actionController, final HumanInteractionRequest request) {
    super(REQUEST);
    this.actionControl = actionController;
    this.request = null;
    this.decision = false;
    initComponents();
    setRequest(request);
  }

  public final void initComponents() {
    this.requestMessageField = new JTextArea();
    var requestConfirmButton = new JButton("Annehmen");
    var requestRefuseButton = new JButton("Ablehnen");

    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setTitle("Status-Nachricht");
    setMaximumSize(new Dimension(260, 160));
    setMinimumSize(new Dimension(260, 160));
    setPreferredSize(new Dimension(260, 160));
    setResizable(false);
    setAlwaysOnTop(true);
    setModal(true);

    this.requestMessageField.setLineWrap(true);
    this.requestMessageField.setEditable(false);

    requestConfirmButton.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(final MouseEvent evt) {
            confirmation();
          }
        });

    requestRefuseButton.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(final MouseEvent evt) {
            refusal();
          }
        });

    addWindowListener(
        new WindowAdapter() {
          @Override
          public void windowClosed(final WindowEvent evt) {
            refusal();
          }
        });

    final var statusDialogLayout = new GroupLayout(getContentPane());
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
                                        PREFERRED_SIZE))
                            .addGroup(
                                statusDialogLayout
                                    .createSequentialGroup()
                                    .addGap(22)
                                    .addComponent(requestConfirmButton)
                                    .addGap(18)
                                    .addComponent(requestRefuseButton)))
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
                    .addGroup(
                        statusDialogLayout
                            .createParallelGroup(BASELINE)
                            .addComponent(requestRefuseButton)
                            .addComponent(requestConfirmButton))
                    .addGap(15)));
    getContentPane().setLayout(statusDialogLayout);
  }

  /**
   * Sets the request.
   *
   * @param request the new request
   */
  public final void setRequest(final HumanInteractionRequest request) {
    String requestToDisplay = null;

    if (request == null) {
      requestToDisplay = "Keine Anfrage angegeben.";
    } else {
      this.request = request;
      requestToDisplay = HumanInteractionRequestTranslator.translate(request);
    }
    this.requestMessageField.setText(requestToDisplay);
  }

  private void confirmation() {
    this.decision = true;
    this.actionControl.evaluateDialog(getDialogType(), this.request.getOriginatorId());
  }

  private void refusal() {
    this.decision = false;
    this.actionControl.evaluateDialog(getDialogType(), this.request.getOriginatorId());
  }

  @Override
  public PacketContent evaluate() {
    PacketContent data = null;

    if (this.request != null) {
      data = this.request.setDecision(this.decision);
    }
    return data;
  }
}
