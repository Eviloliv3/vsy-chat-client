package de.vsy.client.packet_processing.content_processing;

import de.vsy.client.controlling.data_access_interfaces.StatusDataModelAccess;
import de.vsy.client.packet_processing.ResultingContentHandlingProvider;
import de.vsy.shared_module.packet_processing.ContentProcessor;
import de.vsy.shared_transmission.packet.content.status.ContactStatusChangeDTO;

/**
 * PacketProcessor for client status type Packet.
 *
 * <p>Frederic Heath
 */
public class ContactStatusChangeProcessor implements ContentProcessor<ContactStatusChangeDTO> {

  private final StatusDataModelAccess dataModel;

  /**
   * Instantiates a new client status change handler.
   *
   * @param dataModel the dataManagement model
   */
  public ContactStatusChangeProcessor(final StatusDataModelAccess dataModel,
      final ResultingContentHandlingProvider handlerProvider) {
    this.dataModel = dataModel;
  }

  @Override
  public void processContent(ContactStatusChangeDTO toProcess) {

    if (toProcess.getOnlineStatus()) {
      this.dataModel.addContactData(
          toProcess.getContactType(), toProcess.getContactData(), toProcess.getMessages());
    } else {
      this.dataModel.removeContactData(toProcess.getContactType(), toProcess.getContactData());
    }
  }
}
