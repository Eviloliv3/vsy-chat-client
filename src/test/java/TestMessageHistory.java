import static de.vsy.shared_transmission.packet.content.relation.EligibleContactEntity.CLIENT;

import de.vsy.client.gui.MessageListRenderer;
import de.vsy.client.gui.essential_graphical_unit.MessageHistory;
import de.vsy.shared_transmission.packet.content.chat.TextMessageDTO;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class TestMessageHistory {
  final JFrame mainFrame = new JFrame("TestFrame");
  final JPanel mainPanel = new JPanel();

  {
    mainFrame.add(mainPanel);
  }

  public static void main(String... args){
    new TestMessageHistory().createMessageHistoryList();
  }

  void createMessageHistoryList(){
    var list = new JList<TextMessageDTO>();
    JScrollPane scroller = new JScrollPane(list);
    mainPanel.add(scroller);
    var messages = new DefaultListModel<TextMessageDTO>();
    list.setModel(messages);
    list.setCellRenderer(new MessageListRenderer());

    var message1_1 = new TextMessageDTO(15000, CLIENT, 15001, "Textnachricht.Textnachricht.Textnachricht.Textnachricht.Textnachricht.Textnachricht.Textnachricht.");
    var message1_2 = new TextMessageDTO(15000, CLIENT, 15001, "Textnachricht.");
    var message2_1 = new TextMessageDTO(15001, CLIENT, 15000, "Textnachricht.");
    var message2_2 = new TextMessageDTO(15001, CLIENT, 15000, "Textnachricht.Textnachricht.Textnachricht.Textnachricht.Textnachricht.");

    messages.addElement(message1_1);
    messages.addElement(message2_1);
    messages.addElement(message1_2);
    messages.addElement(message2_2);
    messages.addElement(message2_2);
    messages.addElement(message1_1);
    messages.addElement(message2_2);
    messages.addElement(message1_2);
    messages.addElement(message2_1);
    messages.addElement(message1_2);

    showMainFrame();
  }
  void createAndShowMessageHistory(){
  final var m = new MessageHistory();
  m.addClientMessage("Testnachricht.");
    m.addClientMessage("Testnachricht.");
    m.addContactMessage("Testnachricht.");
    m.addClientMessage("Testnachricht.");
    m.addContactMessage("Testnachricht.");
  mainPanel.add(m);
  showMainFrame();
}

private void showMainFrame(){
    SwingUtilities.invokeLater(() -> {
    mainFrame.validate();
    mainFrame.pack();
    mainFrame.setVisible(true);
  });
}
}
