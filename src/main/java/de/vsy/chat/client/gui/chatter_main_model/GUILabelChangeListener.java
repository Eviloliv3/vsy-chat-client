/*
 *
 */
package de.vsy.chat.client.gui.chatter_main_model;

import de.vsy.chat.transmission.dto.CommunicatorDTO;

/**
 * The listener interface for receiving GUILabelChange events. When the GUILabelChange event occurs,
 * that object's appropriate method is invoked.
 */
public interface GUILabelChangeListener {

  /**
   * Sets the new chat label.
   *
   * @param contactData the new new chat label
   */
  public void setNewChatLabel(CommunicatorDTO contactData);

  /**
   * Sets the new client label.
   *
   * @param clientData the new new client label
   */
  public void setNewClientLabel(CommunicatorDTO clientData);
}
