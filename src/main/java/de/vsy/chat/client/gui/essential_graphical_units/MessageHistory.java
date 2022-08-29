/*
 *
 */
package de.vsy.chat.client.gui.essential_graphical_units;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import de.vsy.chat.client.gui.essential_graphical_units.interfaces.ScrollableMessageHistory;

import java.awt.*;
import static javax.swing.Box.createVerticalBox;
import static javax.swing.Box.createVerticalStrut;
import static javax.swing.BoxLayout.Y_AXIS;

/** Custom JPanel to outline currently selected chat history. */
public class MessageHistory extends JPanel implements ScrollableMessageHistory {

  private static final int MAX_MESSAGE_COUNT = 1000;
  private static final long serialVersionUID = -1904605382293445635L;
  private final Box messageHistoryContainer;
  private int messageCounter;

  public MessageHistory() {
    this.messageHistoryContainer = createVerticalBox();
    this.messageCounter = 0;
    setLayout(new BoxLayout(this, Y_AXIS));
    setSize(523, 308);
    setAutoscrolls(true);
  }

  @Override
  public void addClientMessage(final String text) {
    JPanel newClientMessageContainer;

    newClientMessageContainer = chatBubbleContent(text);
    newClientMessageContainer.setAlignmentX(RIGHT_ALIGNMENT);

    addMessage(newClientMessageContainer);
  }

  @Override
  public void addContactMessage(final String text) {
    addContactMessage(text, null);
  }

  @Override
  public void addContactMessage(final String text, final String contactName) {
    JPanel newContactMessageContainer;
    final var contactAndMessage = new StringBuilder();

    if (contactName != null) {
      contactAndMessage.append(contactName).append(":\n");
    }

    if (text != null) {
      contactAndMessage.append(text);

      newContactMessageContainer = chatBubbleContent(contactAndMessage.toString());
      newContactMessageContainer.setAlignmentX(LEFT_ALIGNMENT);

      addMessage(newContactMessageContainer);
    }
  }

  @Override
  public void clearMessageHistory() {
    this.messageHistoryContainer.removeAll();
  }

  /**
   * Chat bubble content.
   *
   * @param text the text
   * @return the j panel
   */
  private JPanel chatBubbleContent(final String text) {
    final var newMessageBubble = new JPanel();
    final var textLabelContent = new StringBuilder();

    textLabelContent
        .append("<html><p style = \"width : 180px\">")
        .append(text)
        .append("</p></html>");

    newMessageBubble.setLayout(new BoxLayout(newMessageBubble, Y_AXIS));

    final var messageArea = new JLabel(textLabelContent.toString());
    messageArea.setBackground(new Color(0.5f, 0.5f, 1f));
    messageArea.setOpaque(true);
    messageArea.setBorder(new EmptyBorder(10, 10, 10, 10));

    newMessageBubble.add(messageArea);
    return newMessageBubble;
  }

  /**
   * Adds the message.
   *
   * @param newMessagePanel the new message panel
   */
  private void addMessage(final JPanel newMessagePanel) {
    this.messageHistoryContainer.add(newMessagePanel);
    this.messageHistoryContainer.add(createVerticalStrut(8));

    if (this.messageCounter >= MAX_MESSAGE_COUNT) {
      this.messageHistoryContainer.remove(0);
    } else {
      this.messageCounter++;
    }
    this.messageHistoryContainer.revalidate();
    this.messageHistoryContainer.repaint();

    add(this.messageHistoryContainer);

    if (this.getParent() instanceof JScrollPane) {
      this.getParent().revalidate();
      this.getParent().repaint();
    }
  }
}
