package de.vsy.client.packet_processing.content_processing;

import de.vsy.client.controlling.data_access_interfaces.StatusDataModelAccess;
import de.vsy.shared_module.packet_processing.ContentProcessor;
import de.vsy.shared_transmission.packet.content.PacketContent;
import de.vsy.shared_transmission.packet.content.relation.ContactRelationRequestDTO;
import de.vsy.shared_utility.id_manipulation.IdComparator;

public class ContactRelationRequestProcessor
    implements ContentProcessor<ContactRelationRequestDTO> {

  private final StatusDataModelAccess dataModel;

  public ContactRelationRequestProcessor(final StatusDataModelAccess dataModel) {
    this.dataModel = dataModel;
  }

  @Override
  public PacketContent processContent(ContactRelationRequestDTO validatedContent) {
    PacketContent processedData = null;
    final var clientId = this.dataModel.getClientId();
    final var iAmOriginator =
        IdComparator.determineIfOriginator(clientId, validatedContent.getOriginatorId());
    if (!iAmOriginator) {
        this.dataModel.removeContactData(
            validatedContent.getContactType(), validatedContent.getRequestingClient());
        this.dataModel.addNotification(validatedContent);
    } else {
      processedData = validatedContent;
    }
    return processedData;
  }
}
