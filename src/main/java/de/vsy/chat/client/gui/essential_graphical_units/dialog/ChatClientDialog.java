/*
 *
 */
package de.vsy.chat.client.gui.essential_graphical_units.dialog;

import de.vsy.chat.transmission.packet.content.PacketContent;

import javax.swing.*;

public abstract class ChatClientDialog extends JDialog {

  private static final long serialVersionUID = -2593595781225288174L;
  private final DialogType type;

  /**
   * Instantiates a new chat client dialog.
   *
   * @param type the type
   */
  protected ChatClientDialog(final DialogType type) {
    this.type = type;
  }

  /**
   * Evaluate.
   *
   * @return the PacketdataManagement
   */
  public abstract PacketContent evaluate();

  /**
   * Gets the dialog type.
   *
   * @return the dialog type
   */
  public DialogType getDialogType() {
    return this.type;
  }
}
