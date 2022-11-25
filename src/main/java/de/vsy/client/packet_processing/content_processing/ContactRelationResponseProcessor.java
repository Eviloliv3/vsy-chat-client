package de.vsy.client.packet_processing.content_processing;

import de.vsy.client.controlling.data_access_interfaces.StatusDataModelAccess;
import de.vsy.shared_module.packet_processing.ContentProcessor;
import de.vsy.shared_transmission.packet.content.PacketContent;
import de.vsy.shared_transmission.packet.content.relation.ContactRelationResponseDTO;
import de.vsy.shared_utility.id_manipulation.IdComparator;

public class ContactRelationResponseProcessor
    implements ContentProcessor<ContactRelationResponseDTO> {

  private final StatusDataModelAccess dataModel;

  public ContactRelationResponseProcessor(final StatusDataModelAccess dataModel) {
    this.dataModel = dataModel;
  }

  @Override
  public PacketContent processContent(ContactRelationResponseDTO toProcess) {
    PacketContent processedData = null;
    final var clientId = this.dataModel.getClientId();
    final var originalRequestData = toProcess.getRequestData();
    final var iAmOriginator =
        IdComparator.determineIfOriginator(clientId, originalRequestData.getOriginatorId());

    if (toProcess.getRequestData().getDesiredState() && toProcess.getDecision()) {
      this.dataModel.addContactData(originalRequestData.getContactType(),
          toProcess.getRespondingClient(), null);
    }

    if (!iAmOriginator) {
      this.dataModel.addNotification(toProcess);
    } else {
      processedData = toProcess;
    }
    return processedData;
  }
}
