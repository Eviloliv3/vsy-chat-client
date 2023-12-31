package de.vsy.client.packet_processing.content_processing;

import de.vsy.client.controlling.data_access_interfaces.StatusDataModelAccess;
import de.vsy.client.packet_processing.ResultingContentHandlingProvider;
import de.vsy.client.packet_processing.ResultingPacketContentHandler;
import de.vsy.shared_module.packet_processing.ContentProcessor;
import de.vsy.shared_transmission.packet.content.relation.ContactRelationRequestDTO;

public class ContactRelationRequestProcessor
    implements ContentProcessor<ContactRelationRequestDTO> {

  private final StatusDataModelAccess dataModel;
  private final ResultingPacketContentHandler contentHandler;

  public ContactRelationRequestProcessor(final StatusDataModelAccess dataModel,
      final ResultingContentHandlingProvider handlerProvider) {
    this.dataModel = dataModel;
    this.contentHandler = handlerProvider.getResultingPacketContentHandler();
  }

  @Override
  public void processContent(ContactRelationRequestDTO validatedContent) {
    final var clientId = this.dataModel.getClientId();
    final var iAmOriginator = clientId == validatedContent.getOriginatorId();

    if (!iAmOriginator) {
      if(!validatedContent.getDesiredState()) {
        this.dataModel.removeContactData(
            validatedContent.getContactType(), validatedContent.getRequestingClient());
        this.dataModel.addNotification(validatedContent);
      }else{
        this.dataModel.addRequest(validatedContent);
      }
    } else {
      this.contentHandler.addRequest(validatedContent);
    }
  }
}
