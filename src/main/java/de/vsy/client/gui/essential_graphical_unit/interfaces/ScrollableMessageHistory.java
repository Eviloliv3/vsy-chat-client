
package de.vsy.client.gui.essential_graphical_unit.interfaces;

/**
 * Provides methods for the manipulation of currently outlined messageHistory manipulation.
 */
public interface ScrollableMessageHistory {

  /**
   * Adds the client message.
   *
   * @param text the text
   */
  void addClientMessage(String text);

  /**
   * Adds the contact message.
   *
   * @param text the text
   */
  void addContactMessage(String text);

  /**
   * Adds the contact message.
   *
   * @param text        the text
   * @param contactName the contact name
   */
  void addContactMessage(String text, String contactName);
}
