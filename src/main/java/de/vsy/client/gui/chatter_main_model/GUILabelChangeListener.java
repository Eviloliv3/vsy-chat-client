/*
 *
 */
package de.vsy.client.gui.chatter_main_model;

import de.vsy.shared_transmission.dto.CommunicatorDTO;

/**
 * The listener interface for receiving GUILabelChange events. When the GUILabelChange event occurs,
 * that object's appropriate method is invoked.
 */
public interface GUILabelChangeListener {

  /**
   * Sets the new chat label.
   *
   * @param contactData the new chat label
   */
  void setChatLabel(CommunicatorDTO contactData);

  /**
   * Sets the new client label.
   *
   * @param clientData the new client label
   */
  void setClientLabel(CommunicatorDTO clientData);
}
