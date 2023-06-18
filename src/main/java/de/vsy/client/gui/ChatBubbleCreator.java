package de.vsy.client.gui;

import static javax.swing.BoxLayout.Y_AXIS;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ChatBubbleCreator {

  private ChatBubbleCreator() {
  }

  public static JPanel createChatBubble(String text) {
    final var newMessageBubble = new JPanel();
    String textLabelContent = "<html><p style = \"width : 180px\">" + text + "</p></html>";
    final var messageArea = new JLabel(textLabelContent);

    newMessageBubble.setLayout(new BoxLayout(newMessageBubble, Y_AXIS));

    messageArea.setOpaque(true);
    newMessageBubble.setBorder(new EmptyBorder(10, 10, 10, 10));

    newMessageBubble.add(messageArea);
    return newMessageBubble;
  }
}
