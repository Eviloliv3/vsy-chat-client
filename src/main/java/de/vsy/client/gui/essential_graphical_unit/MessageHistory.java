package de.vsy.client.gui.essential_graphical_unit;

import static javax.swing.BoxLayout.Y_AXIS;

import de.vsy.client.gui.ChatBubbleCreator;
import de.vsy.client.gui.essential_graphical_unit.interfaces.ScrollableMessageHistory;
import java.io.Serial;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Custom JPanel to outline currently selected chat history.
 */
public class MessageHistory extends Box implements ScrollableMessageHistory {

  private static final int MAX_MESSAGE_COUNT = 1000;
  @Serial
  private static final long serialVersionUID = -1904605382293445635L;
  private int messageCounter;

  public MessageHistory() {
    super(Y_AXIS);
    this.messageCounter = 0;
    setSize(523, 308);
    setAutoscrolls(true);
  }

  @Override
  public void addClientMessage(final String text) {
    JPanel newClientMessageContainer;

    newClientMessageContainer = ChatBubbleCreator.createChatBubble(text);
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

      newContactMessageContainer = ChatBubbleCreator.createChatBubble(contactAndMessage.toString());
      newContactMessageContainer.setAlignmentX(LEFT_ALIGNMENT);

      addMessage(newContactMessageContainer);
    }
  }

  /**
   * Adds the message.
   *
   * @param newMessagePanel the new message panel
   */
  private void addMessage(final JPanel newMessagePanel) {
    SwingUtilities.invokeLater(() -> {
      if (this.messageCounter >= MAX_MESSAGE_COUNT) {
        super.remove(0);
      } else {
        this.messageCounter++;
      }

      if (this.messageCounter > 0) {
        super.add(createVerticalStrut(8));
      }
      super.add(newMessagePanel);
    });
  }
}
