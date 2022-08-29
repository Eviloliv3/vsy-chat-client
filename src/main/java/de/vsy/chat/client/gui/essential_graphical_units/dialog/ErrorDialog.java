/*
 *
 */
package de.vsy.chat.client.gui.essential_graphical_units.dialog;


import static de.vsy.chat.client.gui.essential_graphical_units.dialog.DialogType.INFORMATION;
import static de.vsy.chat.utility.standard_value.StandardIdProvider.STANDARD_SERVER_ID;

import de.vsy.chat.client.controlling.essential_gui_action_interfaces.DialogEssentialActions;
import de.vsy.chat.transmission.packet.content.PacketContent;
import de.vsy.chat.transmission.packet.content.error.ErrorDTO;

import javax.swing.*;
import java.awt.*;
import static java.awt.Font.PLAIN;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import static java.lang.Short.MAX_VALUE;
import static javax.swing.GroupLayout.Alignment.LEADING;
import static javax.swing.GroupLayout.Alignment.TRAILING;
import static javax.swing.GroupLayout.PREFERRED_SIZE;
import static javax.swing.LayoutStyle.ComponentPlacement.RELATED;
import static javax.swing.LayoutStyle.ComponentPlacement.UNRELATED;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

public
class ErrorDialog extends ChatClientDialog {

    private static final long serialVersionUID = 8096076584870253181L;
    private final transient DialogEssentialActions actionControl;
    private JTextArea errorMessageArea;

    /**
     * Instantiates a new error dialog.
     *
     * @param actionController the action controller
     */
    public
    ErrorDialog (final DialogEssentialActions actionController) {
        this(actionController, null);
    }

    /**
     * Instantiates a new error dialog.
     *
     * @param actionController the action controller
     * @param error the error
     */
    public
    ErrorDialog (final DialogEssentialActions actionController,
                 final ErrorDTO error) { // NO_UCD (use default)
        super(INFORMATION);
        this.actionControl = actionController;
        initComponents();
        setErrorText(error);
    }

    public final
    void initComponents () {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setModal(true);
        setSize(new Dimension(260, 260));
        setPreferredSize(new Dimension(260, 270));
        setMinimumSize(new Dimension(260, 260));
        setMaximumSize(new Dimension(260, 260));
        setTitle("Fehlermeldung");
        setAlwaysOnTop(true);

        var errorMessageLabel = new JLabel("Fehler :");
        errorMessageLabel.setFont(new Font("Tahoma", PLAIN, 13));

        var statusMessageAckButton = new JButton("Best\u00E4tigen");
        statusMessageAckButton.addActionListener((final var e) -> confirmation());

        addWindowListener(new WindowAdapter() {
            @Override
            public
            void windowClosed (final WindowEvent evt) {
                confirmation();
            }
        });

        statusMessageAckButton.setPreferredSize(new Dimension(138, 25));
        statusMessageAckButton.setMinimumSize(new Dimension(138, 25));
        statusMessageAckButton.setMaximumSize(new Dimension(138, 25));

        var errorMessageScrollPane = new JScrollPane();
        errorMessageScrollPane.setMinimumSize(new Dimension(154, 23));
        errorMessageScrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);

        var errorCauseScrollPane = new JScrollPane();
        errorCauseScrollPane.setMinimumSize(new Dimension(154, 23));
        errorCauseScrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        final var groupLayout = new GroupLayout(getContentPane());
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(LEADING)
                           .addGroup(groupLayout.createSequentialGroup()
                                                .addGroup(groupLayout.createParallelGroup(LEADING)
                                                                   .addGroup(groupLayout.createSequentialGroup()
                                                                                      .addGap(18)
                                                                                      .addGroup(groupLayout.createParallelGroup(TRAILING)
                                                                                                         .addComponent(errorMessageLabel, LEADING)
                                                                                                         .addComponent(errorMessageScrollPane, LEADING, PREFERRED_SIZE,
                                                                                                                 207, PREFERRED_SIZE)))
                                                                   .addGroup(groupLayout.createSequentialGroup()
                                                                                      .addGap(53)
                                                                                      .addComponent(statusMessageAckButton, PREFERRED_SIZE,
                                                                                              138, PREFERRED_SIZE)))
                                                .addContainerGap(19, MAX_VALUE)));
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(LEADING)
                           .addGroup(groupLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(errorMessageLabel, PREFERRED_SIZE,
                                                              19, PREFERRED_SIZE)
                                                .addPreferredGap(RELATED)
                                                .addComponent(errorMessageScrollPane, PREFERRED_SIZE,
                                                              56, PREFERRED_SIZE)
                                                .addPreferredGap(UNRELATED)
                                                .addComponent(statusMessageAckButton, PREFERRED_SIZE,
                                                              25, PREFERRED_SIZE)
                                                .addContainerGap(41, MAX_VALUE)));

        errorMessageArea = new JTextArea();
        errorMessageArea.setEditable(false);
        errorMessageArea.setWrapStyleWord(true);
        errorMessageArea.setLineWrap(true);
        errorMessageScrollPane.setViewportView(errorMessageArea);

        getContentPane().setLayout(groupLayout);
    }

    /**
     * Sets the error text.
     *
     * @param error the new error text
     */
    public final
    void setErrorText (final ErrorDTO error) {
        final var errorString = new StringBuilder();

        if (error != null) {
            final var origin = error.getOrigin();

            if (origin != null) {
                errorString.append("\nAusloesendes Paket:\n").append(origin);
            }
            errorString.append(error.getMessage());
        } else {
            errorString.append("Kein Fehler angegeben.");
        }

        setErrorMessage(errorString.toString());
    }

    private
    void confirmation () {
        this.actionControl.evaluateDialog(getDialogType(), STANDARD_SERVER_ID);
    }

    /**
     * Sets the error message.
     *
     * @param errorMessage the new error message
     */
    public final
    void setErrorMessage (final String errorMessage) {
        errorMessageArea.setText(errorMessage);
    }

    @Override
    public
    PacketContent evaluate () {
        return null;
    }
}
