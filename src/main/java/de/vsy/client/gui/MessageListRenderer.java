package de.vsy.client.gui;

import de.vsy.shared_transmission.packet.content.chat.TextMessageDTO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.io.Serial;
import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;

/**
 * Custom JPanel to outline currently selected chat history.
 */
public class MessageListRenderer extends JPanel implements ListCellRenderer<TextMessageDTO> {

  @Serial
  private static final long serialVersionUID = -1904605382293445635L;
  private static final int MAX_MESSAGE_COUNT = 100;
  final JTextPane textArea;

  public MessageListRenderer() {
    textArea = new JTextPane();
    add(textArea, BorderLayout.CENTER);
  }

  @Override
  public Component getListCellRendererComponent(JList list, TextMessageDTO value, int index,
      boolean isSelected, boolean cellHasFocus) {
    boolean clientBound = value.getOriginatorId() == 15001;
    textArea.setText(value.getMessage());
    var listWidth = list.getWidth();
    setBackground(list.getBackground());

    if (clientBound) {
      textArea.setBackground(new Color(0.176f, 0.6f, 0.71f));
      setBorder(BorderFactory.createEmptyBorder(0, Math.min(10, (int) (listWidth * 0.10)), 5,
          Math.min(10, (int) (listWidth * 0.05))));
      setAlignmentX(RIGHT_ALIGNMENT);
    } else {
      textArea.setBackground(new Color(0.278f, 0.827f, 0.808f));
      setAlignmentX(LEFT_ALIGNMENT);
      setBorder(BorderFactory.createEmptyBorder(0, Math.min(10, (int) (listWidth * 0.05)), 5,
          Math.min(10, (int) (listWidth * 0.10))));
    }
    if (list.getModel().getSize() >= MAX_MESSAGE_COUNT) {
      list.remove(0);
    }
    return this;
  }
}
