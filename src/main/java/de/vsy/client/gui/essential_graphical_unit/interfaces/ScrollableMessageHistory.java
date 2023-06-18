package de.vsy.client.gui.essential_graphical_unit.interfaces;

/**
 * Provides methods for the manipulation of currently outlined messageHistory manipulation.
 */
public interface ScrollableMessageHistory {

  /**
   * Adds text message with client as originator.
   *
   * @param text the text message
   */
  void addClientMessage(String text);

  /**
   * Adds text message with contact as originator.
   *
   * @param text the text message
   */
  void addContactMessage(String text);

  /**
   * Adds text message with contact as originator and contact display name for distinction
   * purposes.
   *
   * @param text        the text
   * @param contactName the contact name
   */
  void addContactMessage(String text, String contactName);
}
