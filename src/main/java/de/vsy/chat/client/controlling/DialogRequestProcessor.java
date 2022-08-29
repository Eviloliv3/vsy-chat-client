/*
 *
 */
package de.vsy.chat.client.controlling;

import de.vsy.chat.client.gui.essential_graphical_units.dialog.DialogType;

public interface DialogRequestProcessor {

  /**
   * New dialog request.
   *
   * @param type the type
   */
  void newDialogRequest(DialogType type);
}
