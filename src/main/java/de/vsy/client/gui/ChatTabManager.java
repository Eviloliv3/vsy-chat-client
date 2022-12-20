package de.vsy.client.gui;

import de.vsy.client.gui.essential_graphical_unit.interfaces.ScrollableMessageHistory;
import de.vsy.shared_transmission.dto.CommunicatorDTO;

public interface ChatTabManager {

  void addActiveChat(final CommunicatorDTO contact, ScrollableMessageHistory chatHistory);

  void removeActiveChat(final CommunicatorDTO contact);
}
