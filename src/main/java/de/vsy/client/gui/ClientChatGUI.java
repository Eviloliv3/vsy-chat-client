package de.vsy.client.gui;

import static de.vsy.client.gui.essential_graphical_unit.NavigationGoal.ACCOUNT_DELETION;
import static de.vsy.client.gui.essential_graphical_unit.NavigationGoal.CLOSE_APPLICATION;
import static de.vsy.client.gui.essential_graphical_unit.NavigationGoal.CONTACT_ADDITION;
import static de.vsy.client.gui.essential_graphical_unit.NavigationGoal.CONTACT_REMOVAL;
import static de.vsy.client.gui.essential_graphical_unit.NavigationGoal.LOGOUT;
import static de.vsy.shared_transmission.packet.content.relation.EligibleContactEntity.CLIENT;
import static de.vsy.shared_transmission.packet.content.relation.EligibleContactEntity.GROUP;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

import de.vsy.client.controlling.essential_gui_action_interfaces.guiActionInterfaces.GUIChatActions;
import de.vsy.client.data_model.ClientInput;
import de.vsy.client.gui.essential_graphical_unit.MessageHistory;
import de.vsy.client.gui.essential_graphical_unit.interfaces.ScrollableMessageHistory;
import de.vsy.shared_transmission.dto.CommunicatorDTO;
import de.vsy.shared_transmission.packet.content.chat.TextMessageDTO;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedHashMap;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientChatGUI extends JFrame implements ClientInputProvider, ChatTabManager {

  public static final String EMPTY_TITLE = "Not authenticated";
  private static final Logger LOGGER = LogManager.getLogger();
  private final LinkedHashMap<CommunicatorDTO, ScrollableMessageHistory> activeChatTabs;
  private final DefaultListModel<CommunicatorDTO> contactListModel;
  private JTabbedPane chatHistoryTabPane;
  private JList<CommunicatorDTO> contactList;
  private JScrollPane contactListScrollPane;
  private JButton messageInputSendButton;
  private JTextArea messageInputField;
  private JMenuItem signOffItem;
  private JMenuItem accountDeletionItem;
  private JMenuItem closeApplicationItem;
  private JMenuItem contactAdditionItem;
  private JMenuItem contactRemovalItem;

  public ClientChatGUI() {
    this.activeChatTabs = new LinkedHashMap<>();
    this.contactListModel = new DefaultListModel<>();
    initComponents();
  }

  private void initComponents() {
    this.chatHistoryTabPane = new JTabbedPane();
    this.contactList = new JList<>();
    this.messageInputField = new JTextArea();
    this.contactListScrollPane = new JScrollPane();
    this.messageInputSendButton = new JButton();
    this.signOffItem = new JMenuItem();
    this.accountDeletionItem = new JMenuItem();
    this.closeApplicationItem = new JMenuItem();
    this.contactAdditionItem = new JMenuItem();
    this.contactRemovalItem = new JMenuItem();
    JPanel chatterLabelPanel = new JPanel();
    JPanel messageInputInteraction = new JPanel();
    JPanel messagePanel = new JPanel();
    JScrollPane messageInputScrollPane = new JScrollPane();
    JMenuBar chatterMenuBar = new JMenuBar();
    JMenu settingsMenu = new JMenu();
    JMenu contactManipulationMenu = new JMenu();
    JPopupMenu.Separator jSeparator1 = new JPopupMenu.Separator();
    JPopupMenu.Separator jSeparator2 = new JPopupMenu.Separator();
    JPopupMenu.Separator jSeparator3 = new JPopupMenu.Separator();
    JLabel chatterLabel = new JLabel();
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    chatterLabelPanel.setPreferredSize(new java.awt.Dimension(176, 25));

    chatterLabel.setFont(new java.awt.Font("Noto Sans", Font.BOLD, 18));
    chatterLabel.setHorizontalAlignment(SwingConstants.CENTER);
    chatterLabel.setText("Java Chatter");

    GroupLayout chatterLabelPanelLayout = new GroupLayout(chatterLabelPanel);
    chatterLabelPanel.setLayout(chatterLabelPanelLayout);
    chatterLabelPanelLayout.setHorizontalGroup(
        chatterLabelPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(chatterLabel, GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
    );
    chatterLabelPanelLayout.setVerticalGroup(
        chatterLabelPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(chatterLabel, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE,
                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );

    this.contactList.setModel(this.contactListModel);
    this.contactList.setCellRenderer(new ContactListRenderer());
    this.contactList.setSelectionMode(SINGLE_SELECTION);
    this.contactListScrollPane.setViewportView(this.contactList);

    this.messageInputField.setColumns(20);
    this.messageInputField.setRows(3);
    this.messageInputField.setLineWrap(true);
    this.messageInputField.setWrapStyleWord(true);
    messageInputScrollPane.setViewportView(this.messageInputField);

    this.messageInputSendButton.setFont(new java.awt.Font("Noto Sans", Font.BOLD, 14));
    this.messageInputSendButton.setText("Send");

    GroupLayout messageInputInteractionLayout = new GroupLayout(messageInputInteraction);
    messageInputInteraction.setLayout(messageInputInteractionLayout);
    messageInputInteractionLayout.setHorizontalGroup(
        messageInputInteractionLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(messageInputInteractionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(this.messageInputSendButton)
                .addContainerGap())
    );
    messageInputInteractionLayout.setVerticalGroup(
        messageInputInteractionLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING,
                messageInputInteractionLayout.createSequentialGroup()
                    .addGap(16, 16, 16)
                    .addComponent(this.messageInputSendButton, GroupLayout.PREFERRED_SIZE, 35,
                        GroupLayout.PREFERRED_SIZE)
                    .addGap(15, 15, 15))
    );

    GroupLayout messagePanelLayout = new GroupLayout(messagePanel);
    messagePanel.setLayout(messagePanelLayout);
    messagePanelLayout.setHorizontalGroup(
        messagePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(messagePanelLayout.createSequentialGroup()
                .addComponent(messageInputScrollPane, GroupLayout.DEFAULT_SIZE, 416,
                    Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(messageInputInteraction, GroupLayout.PREFERRED_SIZE,
                    GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
    );
    messagePanelLayout.setVerticalGroup(
        messagePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(messageInputScrollPane, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(messageInputInteraction, GroupLayout.DEFAULT_SIZE,
                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );

    chatterMenuBar.setPreferredSize(new java.awt.Dimension(0, 25));

    settingsMenu.setText("Settings");

    this.signOffItem.setText("Sign Off");
    this.signOffItem.setActionCommand(LOGOUT.toString());
    this.accountDeletionItem.setText("Delete Account");
    this.accountDeletionItem.setActionCommand(ACCOUNT_DELETION.toString());
    this.closeApplicationItem.setText("Close Chatter");
    this.closeApplicationItem.setActionCommand(CLOSE_APPLICATION.toString());
    settingsMenu.add(this.signOffItem);
    settingsMenu.add(jSeparator1);
    settingsMenu.add(this.accountDeletionItem);
    settingsMenu.add(jSeparator2);
    settingsMenu.add(this.closeApplicationItem);

    chatterMenuBar.add(settingsMenu);

    contactManipulationMenu.setText("Contacts");

    this.contactAdditionItem.setText("Add Contact");
    this.contactAdditionItem.setActionCommand(CONTACT_ADDITION.toString());
    this.contactRemovalItem.setText("Remove Contact");
    this.contactRemovalItem.setActionCommand(CONTACT_REMOVAL.toString());
    contactManipulationMenu.add(this.contactAdditionItem);
    contactManipulationMenu.add(jSeparator3);
    contactManipulationMenu.add(this.contactRemovalItem);

    chatterMenuBar.add(contactManipulationMenu);

    setJMenuBar(chatterMenuBar);

    GroupLayout layout = new GroupLayout(getContentPane());
    super.getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(chatterLabelPanel, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(this.contactListScrollPane, GroupLayout.PREFERRED_SIZE, 0,
                        Short.MAX_VALUE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(this.chatHistoryTabPane)
                    .addComponent(messagePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
                        Short.MAX_VALUE))
                .addContainerGap())
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(this.chatHistoryTabPane)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(messagePanel, GroupLayout.PREFERRED_SIZE,
                            GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(chatterLabelPanel, GroupLayout.PREFERRED_SIZE,
                            GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(this.contactListScrollPane, GroupLayout.DEFAULT_SIZE, 482,
                            Short.MAX_VALUE)))
                .addContainerGap())
    );
  }

  public void addInteractionLogic(final GUIChatActions chatActions,
      final ActionListener menuListener) {
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

    this.signOffItem.addActionListener(menuListener);
    this.closeApplicationItem.addActionListener(menuListener);
    this.accountDeletionItem.addActionListener(menuListener);
    this.contactRemovalItem.addActionListener(menuListener);
    this.contactAdditionItem.addActionListener(menuListener);
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        menuListener.actionPerformed(
            new ActionEvent(e.getSource(), e.getID(), CLOSE_APPLICATION.toString()));
        dispose();
      }
    });
    this.contactList.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        chatActions.chooseContact(e);
      }
    });

    this.messageInputSendButton.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        chatActions.sendMessage();
      }
    });
  }

  @Override
  public ClientInput<String> getInput() {
    final var activeChatContacts = this.activeChatTabs.keySet();
    var activeChatArray = new CommunicatorDTO[activeChatContacts.size()];
    activeChatContacts.toArray(activeChatArray);
    final var currentTabIndex = this.chatHistoryTabPane.getSelectedIndex();

    final var contact = activeChatArray[currentTabIndex];
    final var content = this.messageInputField.getText();
    this.messageInputField.setText("");
    return new ClientInput<>(content, contact.getCommunicatorId());
  }

  public void resetData() {
    SwingUtilities.invokeLater(() -> {
      this.setClientTitle(EMPTY_TITLE);
      this.contactListModel.removeAllElements();
      this.chatHistoryTabPane.removeAll();
    });
    this.activeChatTabs.clear();
  }

  @Override
  public CommunicatorDTO getSelectedContact() {
    return this.contactList.getSelectedValue();
  }

  public void addContact(final int index, final CommunicatorDTO contact) {
    SwingUtilities.invokeLater(() -> this.contactListModel.add(index, contact));
  }

  public void removeContact(final CommunicatorDTO contact) {
    SwingUtilities.invokeLater(() -> {
      this.contactListModel.removeElement(contact);
      this.removeActiveChat(contact);
    });
  }

  @Override
  public void addActiveChat(final CommunicatorDTO contact, MessageHistory chatHistory) {
    if (!(this.activeChatTabs.containsKey(contact))) {
      JScrollPane chatHistoryScrollPane = new JScrollPane(VERTICAL_SCROLLBAR_AS_NEEDED,
          HORIZONTAL_SCROLLBAR_NEVER);
      chatHistoryScrollPane.setAutoscrolls(true);
      chatHistoryScrollPane.setViewportView(chatHistory);
      this.activeChatTabs.put(contact, chatHistory);
      SwingUtilities.invokeLater(
          () -> {
            this.chatHistoryTabPane.addTab(contact.getDisplayLabel(), chatHistoryScrollPane);
            chatHistory.requestFocusInWindow();
          });
    } else {
      SwingUtilities.invokeLater(
          () -> {
            final var contactChatIndex = this.chatHistoryTabPane.indexOfTab(
                contact.getDisplayLabel());
            final var contactChatTab = this.chatHistoryTabPane.getTabComponentAt(contactChatIndex);
            contactChatTab.requestFocusInWindow();
          });
    }
  }

  @Override
  public void removeActiveChat(final CommunicatorDTO contact) {
      SwingUtilities.invokeLater(() -> {
        var contactTabIndex = this.chatHistoryTabPane.indexOfTab(contact.getDisplayLabel());
        if (contactTabIndex > 0) {
        this.chatHistoryTabPane.removeTabAt(contactTabIndex);
          this.activeChatTabs.remove(contact);
        } else {
          LOGGER.error("No chat tab found for {}.", contact);
        }
      });
  }

  public void setClientTitle(final String clientTitle) {
    SwingUtilities.invokeLater(() -> super.setTitle(clientTitle));
  }

  public void addMessage(final String contactName, final boolean clientBound,
      final TextMessageDTO message) {
    ScrollableMessageHistory messageHistory = null;

    for (var messageTab : this.activeChatTabs.entrySet()) {
      if (contactName.equals(messageTab.getKey().getDisplayLabel())) {
        messageHistory = messageTab.getValue();
      }
    }

    if (messageHistory != null) {
      var messageContent = message.getMessage();
      var isGroupMessage = message.getContactType().equals(GROUP);

      if (isGroupMessage) {
        if (clientBound) {
          messageHistory.addClientMessage(messageContent);
        } else {
          messageHistory.addContactMessage(contactName, messageContent);
        }
      } else {
        if (clientBound) {
          messageHistory.addClientMessage(messageContent);
        } else {
          messageHistory.addContactMessage(messageContent);
        }
      }
    } else {
      LOGGER.trace("No active chat tab found for {}. Message discarded.", contactName);
    }
  }
}
