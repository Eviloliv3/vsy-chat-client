/*
 *
 */
package de.vsy.chat.client.packet_processing.content_processing;

import de.vsy.chat.transmission.packet.content.PacketContent;
import de.vsy.chat.transmission.packet.content.status.ContactMessengerStatusDTO;
import de.vsy.chat.client.controlling.data_access_interfaces.StatusDataModelAccess;
import de.vsy.chat.module.packet_exception.PacketProcessingException;
import de.vsy.chat.module.packet_processing.ContentProcessor;

/**
 * PacketProcessor for client status type Packet.
 *
 * <p>Frederic Heath
 */
public class ContactStatusChangeProcessor implements ContentProcessor<ContactMessengerStatusDTO> {

  private final StatusDataModelAccess dataModel;

  /**
   * Instantiates a new client status change handler.
   *
   * @param dataModel the dataManagement model
   */
  public ContactStatusChangeProcessor(final StatusDataModelAccess dataModel) {
    this.dataModel = dataModel;
  }

  @Override
  public PacketContent processContent(ContactMessengerStatusDTO toProcess)
      throws PacketProcessingException {

    if (toProcess.getOnlineStatus()) {
      this.dataModel.addContactData(
          toProcess.getContactType(), toProcess.getContactData(), toProcess.getMessages());
    } else {
      this.dataModel.removeContactData(toProcess.getContactType(), toProcess.getContactData());
    }
    return null;
  }
}
