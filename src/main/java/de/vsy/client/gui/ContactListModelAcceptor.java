package de.vsy.client.gui;

import de.vsy.shared_transmission.dto.CommunicatorDTO;
import javax.swing.AbstractListModel;

public interface ContactListModelAcceptor {

  public void setContactListModel(AbstractListModel<CommunicatorDTO> contactList);
}
