/*
 *
 */
package de.vsy.client.gui.external_gui_access_interfaces;

/**
 * Grants access to GUI field indicating whether it was closed or not.
 */
public interface CloseApplicationFlagProvider {

  /**
   * Gets the close flag.
   *
   * @return the close flag
   */
  boolean getCloseFlag();

  void setCloseFlag();
}
