/*
 *
 */
package de.vsy.chat.client.gui.external_gui_access_interfaces;

/** Grants access to GUI field indicating whether it was closed or not. */
public interface CloseApplicationFlagProvider { // NO_UCD
  // (use
  // default)

  /**
   * Gets the close flag.
   *
   * @return the close flag
   */
  public boolean getCloseFlag();

  public void setCloseFlag();
}
