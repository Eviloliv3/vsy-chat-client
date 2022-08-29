/*
 *
 */
package de.vsy.chat.client.gui.chatter_main_model;

import de.vsy.chat.transmission.dto.CommunicatorDTO;
import de.vsy.chat.client.controlling.essential_gui_action_interfaces.GUIActions;
import de.vsy.chat.client.gui.chatter_main_model.customRendering.ContactListRenderer;
import de.vsy.chat.client.gui.essential_graphical_units.MessageHistory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static de.vsy.chat.utility.standard_value.StandardIdProvider.STANDARD_CLIENT_ID;
import static java.lang.Short.MAX_VALUE;
import static javax.swing.GroupLayout.Alignment.LEADING;
import static javax.swing.GroupLayout.Alignment.TRAILING;
import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;
import static javax.swing.LayoutStyle.ComponentPlacement.RELATED;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

public class ClientChatGUI extends JFrame
    implements Runnable, GUILabelChangeListener, GUILogicRelevantDataProvider {

  private static final long serialVersionUID = -2230576956326479400L;
  private volatile boolean guiClosedFlag;
  private transient GUIActions guiController;
  private JLabel activeChatLabel;
  private JList<CommunicatorDTO> contactList;
  private JScrollPane contactListScrollBar;
  private JMenuBar menuBar;
  private JScrollPane messageHistoryScrollBar;
  private JTextArea messageInput;

  public ClientChatGUI() {
    guiClosedFlag = false;
    initComponents();
    initContactlist();

    validate();
    pack();
  }

  private void initComponents() {
    setResizable(false);
    var chatBasePanel = new JPanel();

    this.messageHistoryScrollBar =
        new JScrollPane(VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER);
    this.messageHistoryScrollBar.setAutoscrolls(true);
    this.activeChatLabel = new JLabel();
    var chatLabel = new JLabel();
    var messageInputScrollBar = new JScrollPane();
    this.messageInput = new JTextArea();
    this.messageInput.setWrapStyleWord(true);
    this.contactListScrollBar = new JScrollPane();
    var messageInputSend = new JButton();

    this.messageHistoryScrollBar.setMinimumSize(new Dimension(523, 308));
    this.messageHistoryScrollBar.setPreferredSize(new Dimension(523, 308));
    this.messageHistoryScrollBar.setSize(new Dimension(523, 308));

    chatLabel.setFont(new Font("SansSerif", 0, 12)); // NOI18N
    chatLabel.setText("Java - Chatter");

    this.messageInput.setColumns(20);
    this.messageInput.setLineWrap(true);
    this.messageInput.setRows(3);
    this.messageInput.setTabSize(4);
    messageInputScrollBar.setViewportView(this.messageInput);

    this.contactListScrollBar.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
    this.contactListScrollBar.setMinimumSize(new Dimension(0, 0));

    messageInputSend.setText("Send");
    messageInputSend.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(final MouseEvent evt) {
            guiController.sendMessage();
          }
        });

    final var charBasePanel = new GroupLayout(chatBasePanel);
    charBasePanel.setHorizontalGroup(
        charBasePanel
            .createParallelGroup(LEADING)
            .addGroup(
                charBasePanel
                    .createSequentialGroup()
                    .addContainerGap()
                    .addGroup(
                        charBasePanel
                            .createParallelGroup(LEADING)
                            .addComponent(
                                this.contactListScrollBar, PREFERRED_SIZE, 150, PREFERRED_SIZE)
                            .addComponent(chatLabel))
                    .addPreferredGap(RELATED)
                    .addGroup(
                        charBasePanel
                            .createParallelGroup(LEADING)
                            .addGroup(
                                charBasePanel
                                    .createSequentialGroup()
                                    .addComponent(
                                        this.messageHistoryScrollBar,
                                        DEFAULT_SIZE,
                                        DEFAULT_SIZE,
                                        MAX_VALUE)
                                    .addContainerGap())
                            .addGroup(
                                charBasePanel
                                    .createSequentialGroup()
                                    .addComponent(
                                        this.activeChatLabel, PREFERRED_SIZE, 210, PREFERRED_SIZE)
                                    .addGap(0, 325, MAX_VALUE))
                            .addGroup(
                                charBasePanel
                                    .createSequentialGroup()
                                    .addComponent(
                                        messageInputScrollBar, PREFERRED_SIZE, 420, PREFERRED_SIZE)
                                    .addPreferredGap(RELATED)
                                    .addComponent(messageInputSend, DEFAULT_SIZE, 97, MAX_VALUE)
                                    .addContainerGap()))));
    charBasePanel.setVerticalGroup(
        charBasePanel
            .createParallelGroup(LEADING)
            .addGroup(
                charBasePanel
                    .createSequentialGroup()
                    .addGap(7)
                    .addGroup(
                        charBasePanel
                            .createParallelGroup(LEADING, false)
                            .addComponent(chatLabel, PREFERRED_SIZE, 35, PREFERRED_SIZE)
                            .addGroup(
                                charBasePanel
                                    .createSequentialGroup()
                                    .addGap(5)
                                    .addComponent(
                                        this.activeChatLabel,
                                        DEFAULT_SIZE,
                                        DEFAULT_SIZE,
                                        MAX_VALUE)))
                    .addPreferredGap(RELATED)
                    .addGroup(
                        charBasePanel
                            .createParallelGroup(TRAILING)
                            .addGroup(
                                charBasePanel
                                    .createSequentialGroup()
                                    .addComponent(
                                        this.messageHistoryScrollBar,
                                        DEFAULT_SIZE,
                                        DEFAULT_SIZE,
                                        MAX_VALUE)
                                    .addPreferredGap(RELATED)
                                    .addGroup(
                                        charBasePanel
                                            .createParallelGroup(LEADING)
                                            .addComponent(
                                                messageInputScrollBar,
                                                PREFERRED_SIZE,
                                                54,
                                                PREFERRED_SIZE)
                                            .addComponent(
                                                messageInputSend,
                                                PREFERRED_SIZE,
                                                42,
                                                PREFERRED_SIZE))
                                    .addContainerGap())
                            .addComponent(
                                this.contactListScrollBar, DEFAULT_SIZE, 380, MAX_VALUE))));

    chatBasePanel.setLayout(charBasePanel);

    this.messageHistoryScrollBar
        .getAccessibleContext()
        .setAccessibleName("messageHistoryScrollBar");
    this.contactListScrollBar.getAccessibleContext().setAccessibleName("contactListScrollBar");

    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLocation(new Point(300, 250));

    final var layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout
            .createParallelGroup(LEADING)
            .addComponent(chatBasePanel, DEFAULT_SIZE, DEFAULT_SIZE, MAX_VALUE));
    layout.setVerticalGroup(
        layout
            .createParallelGroup(LEADING)
            .addGroup(
                TRAILING,
                layout
                    .createSequentialGroup()
                    .addComponent(chatBasePanel, DEFAULT_SIZE, DEFAULT_SIZE, MAX_VALUE)
                    .addContainerGap()));

    addWindowListener(
        new WindowAdapter() {
          @Override
          public void windowClosing(final WindowEvent evt) {
            guiController.endProgram();
          }
        });
  }

  private void initContactlist() {
    this.contactList = new JList<>();
    this.contactListScrollBar.add(this.contactList);
    this.contactList.setCellRenderer(new ContactListRenderer());
    this.contactList.setSelectionMode(SINGLE_SELECTION);
    this.contactList.setToolTipText("");
    this.contactList.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(final MouseEvent evt) {
            guiController.chooseContact(evt);
          }
        });
    this.contactList.getAccessibleContext().setAccessibleName("");
    this.contactListScrollBar.setViewportView(this.contactList);
  }

  @Override
  public String getMessage() {
    final var message = this.messageInput.getText();
    this.messageInput.setText("");
    return message;
  }

  @Override
  public CommunicatorDTO getSelectedContact() {
    return this.contactList.getSelectedValue();
  }

  /**
   * Inits the GUI control.
   *
   * @param guiController the gui controller
   * @param chatterMenu the chatter menu
   */
  public void initGUIControl(final GUIActions guiController, final JMenuBar chatterMenu) {

    if (this.guiController == null) {
      setGUIActionController(guiController);
    }

    if (this.menuBar == null) {
      setMenuBar(chatterMenu);
    }
  }

  /**
   * Sets the GUI action controller.
   *
   * @param guiController the new GUI action controller
   */
  private void setGUIActionController(final GUIActions guiController) {
    this.guiController = guiController;
  }

  /**
   * Sets the menu bar.
   *
   * @param chatterMenu the new menu bar
   */
  private void setMenuBar(final JMenuBar chatterMenu) {
    this.menuBar = chatterMenu;
    setJMenuBar(this.menuBar);
  }

  @Override
  public void run() {
    Thread.currentThread().setName("GUI-Thread");
    setLocationRelativeTo(null);
    setVisible(true);
  }

  /**
   * Sets the chat label.
   *
   * @param newLabel the new chat label
   */
  public void setChatLabel(final String newLabel) {
    this.activeChatLabel.setText(newLabel);
  }

  /**
   * Sets the contact list model.
   *
   * @param contactList the new contact list model
   */
  public void setContactListModel(final DefaultListModel<CommunicatorDTO> contactList) {
    this.contactList.setModel(contactList);
  }

  /**
   * Sets the message history.
   *
   * @param messages the new message history
   */
  public void setMessageHistory(final MessageHistory messages) {
    this.messageHistoryScrollBar.add(messages);
    this.messageHistoryScrollBar.setViewportView(messages);
    pack();
  }

  @Override
  public void setNewChatLabel(final CommunicatorDTO contactData) {
    final var newChatLabel = new StringBuilder();

    if (contactData != null && contactData.getCommunicatorId() != STANDARD_CLIENT_ID) {
      newChatLabel.append(contactData.getDisplayLabel());
      this.messageInput.setEditable(true);
    } else {
      newChatLabel.append("");
      this.messageInput.setEditable(false);
    }

    this.activeChatLabel.setText(newChatLabel.toString());
  }

  // -------------------------------------------
  // ---Data-Change-Listener--------------------
  // -------------------------------------------
  @Override
  public void setNewClientLabel(final CommunicatorDTO contactData) {
    final var sb = new StringBuilder();

    if (contactData != null && contactData.getCommunicatorId() != STANDARD_CLIENT_ID) {
      int clientId;

      if ((clientId = contactData.getCommunicatorId()) != STANDARD_CLIENT_ID) {
        sb.append("Eingeloggt als: ")
            .append(contactData.getDisplayLabel())
            .append(" (ID: ")
            .append(clientId)
            .append(")");
      }
    }

    if (sb.length() == 0) {
      sb.append("Nicht authentifiziert");
    }

    setTitle(sb.toString());
  }
  public static void main(String args[]) {
    /* Set the Nimbus look and feel */
    //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
    /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
     */
    try {
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
    } catch (ClassNotFoundException ex) {
        java.util.logging.Logger.getLogger(ClientChatGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
        java.util.logging.Logger.getLogger(ClientChatGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
        java.util.logging.Logger.getLogger(ClientChatGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
        java.util.logging.Logger.getLogger(ClientChatGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
    public void run() {
        new ClientChatGUI().setVisible(true);
    }
    });
}
}
