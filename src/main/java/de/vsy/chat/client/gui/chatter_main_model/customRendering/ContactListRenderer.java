/*
 *
 */
package de.vsy.chat.client.gui.chatter_main_model.customRendering;

import de.vsy.chat.transmission.dto.CommunicatorDTO;

import javax.swing.*;
import java.awt.*;
import static java.lang.System.err;

/**
 * Custom renderer for ClientData objects.
 *
 * <p>Frederic Heath
 */
public class ContactListRenderer extends JLabel implements ListCellRenderer<Object> {

  private static final long serialVersionUID = -9047658157420599248L;
  private int clientId;

  public ContactListRenderer() {
    setOpaque(true);
  }

  /**
   * Gets the client id.
   *
   * @return the client id
   */
  public int getCommunicatorId() {
    return this.clientId;
  }

  @Override
  public Component getListCellRendererComponent(
      final JList<?> list,
      final Object value,
      final int index,
      final boolean isSelected,
      final boolean cellHasFocus) {

    try {
      final var client = (CommunicatorDTO) value;
      setText(client.getDisplayLabel());
      this.clientId = client.getCommunicatorId();
    } catch (final ClassCastException cc) {
      err.println(
          "Element konnte nicht gezeichnet werden: Cast " + "nach TextMessage nicht moeglich");
    }

    if (isSelected) {
      setBackground(list.getSelectionBackground());
      setForeground(list.getSelectionForeground());
    } else {
      setBackground(list.getBackground());
      setForeground(list.getForeground());
    }
    setFont(list.getFont());
    setEnabled(list.isEnabled());

    return this;
  }
}
