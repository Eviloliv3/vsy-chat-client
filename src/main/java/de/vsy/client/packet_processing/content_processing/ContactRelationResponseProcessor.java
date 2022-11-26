package de.vsy.client.packet_processing.content_processing;

import de.vsy.client.controlling.data_access_interfaces.StatusDataModelAccess;
import de.vsy.client.packet_processing.ResultingContentHandlingProvider;
import de.vsy.client.packet_processing.ResultingPacketContentHandler;
import de.vsy.shared_module.packet_processing.ContentProcessor;
import de.vsy.shared_transmission.packet.content.relation.ContactRelationResponseDTO;
import de.vsy.shared_utility.id_manipulation.IdComparator;

public class ContactRelationResponseProcessor
    implements ContentProcessor<ContactRelationResponseDTO> {

  private final StatusDataModelAccess dataModel;
  private final ResultingPacketContentHandler contentHandler;

  public ContactRelationResponseProcessor(final StatusDataModelAccess dataModel,
      final ResultingContentHandlingProvider handlerProvider) {
    this.dataModel = dataModel;
    this.contentHandler = handlerProvider.getResultingPacketContentHandler();
  }

  @Override
  public void processContent(ContactRelationResponseDTO toProcess) {
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
      this.contentHandler.addRequest(toProcess);
    }
  }
}
