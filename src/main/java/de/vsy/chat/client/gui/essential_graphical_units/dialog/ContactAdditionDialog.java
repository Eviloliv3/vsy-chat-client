/*
 *
 */
package de.vsy.chat.client.gui.essential_graphical_units.dialog;

import static de.vsy.chat.client.gui.essential_graphical_units.dialog.DialogType.REQUEST;
import static de.vsy.chat.utility.standard_value.StandardIdProvider.STANDARD_CLIENT_ID;

import de.vsy.chat.client.controlling.essential_gui_action_interfaces.DialogEssentialActions;
import de.vsy.chat.transmission.packet.content.PacketContent;
import de.vsy.chat.transmission.packet.content.error.ErrorDTO;
import de.vsy.chat.transmission.packet.content.relation.ContactRelationRequestDTO;
import de.vsy.chat.transmission.packet.content.relation.EligibleContactEntity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import static java.lang.Integer.parseInt;
import static java.lang.Short.MAX_VALUE;
import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.LEADING;
import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;
import static javax.swing.LayoutStyle.ComponentPlacement.RELATED;

public
class ContactAdditionDialog extends ChatClientDialog {

    private static final long serialVersionUID = -7039554282835949881L;
    private final transient DialogEssentialActions actionControl;
    private JTextField newContactIdField;

    /**
     * Create the dialog.
     *
     * @param actionControl the action control
     */
    public
    ContactAdditionDialog (final DialogEssentialActions actionControl) {
        super(REQUEST);
        this.actionControl = actionControl;
        initComponents();
    }

    private
    void initComponents () {
        var addContactIdLabel = new JLabel();
        this.newContactIdField = new JTextField();
        var addContactButton = new JButton();
        var cancelNewContactButton = new JButton();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Freundschaftsanfrage");
        setMaximumSize(new Dimension(260, 160));
        setPreferredSize(new Dimension(260, 160));
        setMinimumSize(new Dimension(260, 160));
        setResizable(false);
        addContactIdLabel.setText("Kontakt-Id");

        addContactButton.setMinimumSize(new Dimension(100, 25));
        addContactButton.setMaximumSize(new Dimension(100, 25));
        addContactButton.setSize(new Dimension(100, 25));
        addContactButton.setPreferredSize(new Dimension(100, 25));
        addContactButton.addActionListener((final var e) -> {
            confirmation();
        });

        this.newContactIdField = new JTextField();
        this.newContactIdField.setPreferredSize(new Dimension(120, 19));
        this.newContactIdField.setMaximumSize(new Dimension(120, 19));
        this.newContactIdField.setMinimumSize(new Dimension(120, 19));
        this.newContactIdField.setSize(new Dimension(120, 19));
        this.newContactIdField.setColumns(10);

        cancelNewContactButton.addActionListener((final var e) -> {
            cancellation();
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public
            void windowClosed (final WindowEvent evt) {
                cancellation();
            }
        });

        cancelNewContactButton.setSize(new Dimension(100, 25));
        cancelNewContactButton.setPreferredSize(new Dimension(100, 25));
        cancelNewContactButton.setMinimumSize(new Dimension(100, 25));
        cancelNewContactButton.setMaximumSize(new Dimension(100, 25));
        final var groupLayout = new GroupLayout(getContentPane());
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(LEADING)
                           .addGroup(groupLayout.createSequentialGroup()
                                                .addGap(20)
                                                .addGroup(groupLayout.createParallelGroup(LEADING)
                                                                   .addGroup(groupLayout.createSequentialGroup()
                                                                                      .addComponent(addContactButton, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                                                                                      .addPreferredGap(RELATED)
                                                                                      .addComponent(cancelNewContactButton, PREFERRED_SIZE,
                                                                                              100, PREFERRED_SIZE))
                                                                   .addGroup(groupLayout.createSequentialGroup()
                                                                                      .addComponent(
                                                                                              addContactIdLabel)
                                                                                      .addGap(37)
                                                                                      .addComponent(this.newContactIdField, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)))
                                                .addContainerGap(18, MAX_VALUE)));
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(LEADING)
                           .addGroup(groupLayout.createSequentialGroup()
                                                .addGap(40)
                                                .addGroup(groupLayout.createParallelGroup(BASELINE)
                                                                   .addComponent(
                                                                           addContactIdLabel)
                                                                   .addComponent(this.newContactIdField, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
                                                .addPreferredGap(RELATED,
                                                        33, MAX_VALUE)
                                                .addGroup(groupLayout.createParallelGroup(BASELINE)
                                                                   .addComponent(addContactButton, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                                                                   .addComponent(cancelNewContactButton, PREFERRED_SIZE,
                                                                           25, PREFERRED_SIZE))
                                                .addContainerGap()));
        getContentPane().setLayout(groupLayout);

        addContactButton.setText("Anfragen");

        cancelNewContactButton.setText("Abbrechen");
    }

    private
    void confirmation () {
        this.actionControl.evaluateDialog(getDialogType(), getContactId());
    }

    private
    void cancellation () {
        this.actionControl.closeDialog(getDialogType());
    }

    /**
     * Gets the contact id.
     *
     * @return the contact id
     */
    public
    int getContactId () {
        int contactId;

        try {
            contactId = parseInt(this.newContactIdField.getText());
        } catch (final NumberFormatException nfe) {
            System.out.println("Keine Nummer im Kontakt Eingabefeld gefunden.");
            contactId = STANDARD_CLIENT_ID;
        }
        return contactId;
    }

    @Override
    public
    PacketContent evaluate () {
        PacketContent data;
        int contactId;

        contactId = getContactId();

        if (contactId < 0) {
            data = new ErrorDTO("Es wurde keine Freundschaftsanfrage versandt." +
                                "Ihre Eingabe muss eine natuerliche Zahl sein.",
                                null);
        } else {
            data = new ContactRelationRequestDTO(EligibleContactEntity.CLIENT,
                                                 STANDARD_CLIENT_ID, contactId,
                                                 true);
        }
        return data;
    }
}
