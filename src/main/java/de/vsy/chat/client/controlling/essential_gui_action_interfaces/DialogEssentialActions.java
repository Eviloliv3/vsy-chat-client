/*
 *
 */
package de.vsy.chat.client.controlling.essential_gui_action_interfaces;

import de.vsy.chat.client.gui.essential_graphical_units.dialog.DialogType;

/** Provides dialog with options for input processing through GUI logic. */
public interface DialogEssentialActions {

  /**
   * Close dialog.
   *
   * @param dialog the dialog
   */
  void closeDialog(DialogType dialog);

  /**
   * Confirm dialog.
   *
   * @param dialog the dialog
   * @param contactId the contact id
   */
  void evaluateDialog(DialogType dialog, int contactId);
}
