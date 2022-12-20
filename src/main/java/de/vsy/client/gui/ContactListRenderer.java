/*
 *
 */
package de.vsy.client.gui;

import static java.lang.System.err;

import de.vsy.shared_transmission.dto.CommunicatorDTO;
import java.awt.Component;
import java.io.Serial;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * Custom renderer for ClientData objects.
 *
 * <p>Frederic Heath
 */
public class ContactListRenderer extends JLabel implements ListCellRenderer<Object> {

  @Serial
  private static final long serialVersionUID = -9047658157420599248L;

  public ContactListRenderer() {
    setOpaque(true);
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
    } catch (final ClassCastException cc) {
      err.println(
          "CommunicatorDTO could not be be painted: {} " + cc.getClass().getSimpleName()
              + " occurred for " + value);
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
