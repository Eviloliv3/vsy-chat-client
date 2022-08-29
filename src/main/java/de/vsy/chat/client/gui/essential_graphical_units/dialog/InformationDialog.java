/*
 *
 */
package de.vsy.chat.client.gui.essential_graphical_units.dialog;

import static de.vsy.chat.client.gui.essential_graphical_units.dialog.DialogType.INFORMATION;
import static de.vsy.chat.utility.standard_value.StandardIdProvider.STANDARD_SERVER_ID;

import de.vsy.chat.client.controlling.essential_gui_action_interfaces.DialogEssentialActions;
import de.vsy.chat.transmission.packet.content.PacketContent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import static java.lang.Short.MAX_VALUE;
import static javax.swing.GroupLayout.Alignment.LEADING;
import static javax.swing.GroupLayout.Alignment.TRAILING;
import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;
import static javax.swing.LayoutStyle.ComponentPlacement.RELATED;

public
class InformationDialog extends ChatClientDialog { // NO_UCD
    // (use
    // default)
    private static final long serialVersionUID = 6289227728904151492L;
    private final DialogEssentialActions actionControl;
    private JButton statusMessageAckButton;
    private JTextArea statusMessageField;

    /**
     * Instantiates a new information dialog.
     *
     * @param actionController the action controller
     */
    public
    InformationDialog (final DialogEssentialActions actionController) {
        this(actionController, null);
    }

    /**
     * Create the dialog.
     *
     * @param actionController the action controller
     * @param message the message
     */
    public
    InformationDialog (final DialogEssentialActions actionController,
                       final String message) {
        super(INFORMATION);
        this.actionControl = actionController;
        initComponents();
        setMessage(message);
    }

    private
    void initComponents () {
        this.statusMessageField = new JTextArea();
        this.statusMessageAckButton = new JButton("Best\u00E4tigen");

        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Status-Nachricht");
        setMaximumSize(new Dimension(260, 160));
        setMinimumSize(new Dimension(260, 160));
        setPreferredSize(new Dimension(260, 160));

        this.statusMessageField.setLineWrap(true);
        this.statusMessageField.setEditable(false);

        this.statusMessageAckButton.addActionListener((final var e) -> {
            confirmation();
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public
            void windowClosed (final WindowEvent evt) {
                confirmation();
            }
        });

        this.statusMessageAckButton.setMaximumSize(new Dimension(138, 25));
        this.statusMessageAckButton.setMinimumSize(new Dimension(138, 25));
        this.statusMessageAckButton.setPreferredSize(new Dimension(138, 25));
        final var statusDialogLayout = new GroupLayout(getContentPane());
        statusDialogLayout.setHorizontalGroup(statusDialogLayout.createParallelGroup(LEADING)
                                  .addGroup(statusDialogLayout.createSequentialGroup()
                                                            .addGap(18)
                                                            .addComponent(this.statusMessageField, PREFERRED_SIZE,
                                                                    208, PREFERRED_SIZE)
                                                            .addContainerGap(18, MAX_VALUE))
                                  .addGroup(TRAILING,
                                            statusDialogLayout.createSequentialGroup()
                                                              .addGap(53)
                                                              .addComponent(this.statusMessageAckButton, DEFAULT_SIZE, DEFAULT_SIZE, MAX_VALUE)
                                                              .addGap(53)));
        statusDialogLayout.setVerticalGroup(statusDialogLayout.createParallelGroup(LEADING)
                                  .addGroup(statusDialogLayout.createSequentialGroup()
                                                            .addContainerGap()
                                                            .addComponent(this.statusMessageField, PREFERRED_SIZE,
                                                                    73, PREFERRED_SIZE)
                                                            .addPreferredGap(RELATED)
                                                            .addComponent(this.statusMessageAckButton, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                                                            .addContainerGap(DEFAULT_SIZE, MAX_VALUE)));
        getContentPane().setLayout(statusDialogLayout);
    }

    /**
     * Sets the message.
     *
     * @param message the new message
     */
    public final
    void setMessage (final String message) {
        String messageToDisplay = null;

        if (message == null) {
            messageToDisplay = "Keine Nachricht angegeben.";
        } else {
            messageToDisplay = message;
        }
        this.statusMessageField.setText(messageToDisplay);
    }

    private
    void confirmation () {
        this.actionControl.evaluateDialog(getDialogType(), STANDARD_SERVER_ID);
    }

    @Override
    public
    PacketContent evaluate () {
        return null;
    }

    /**
     * Sets the dialog title.
     *
     * @param newTitle the new dialog title
     */
    public
    void setDialogTitle (final String newTitle) {
        setTitle(newTitle);
    }
}
