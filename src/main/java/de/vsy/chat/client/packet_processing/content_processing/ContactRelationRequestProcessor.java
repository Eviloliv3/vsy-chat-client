package de.vsy.chat.client.packet_processing.content_processing;

import de.vsy.chat.transmission.packet.content.PacketContent;
import de.vsy.chat.transmission.packet.content.relation.ContactRelationRequestDTO;
import de.vsy.chat.utility.id_manipulation.IdComparator;
import de.vsy.chat.client.controlling.data_access_interfaces.StatusDataModelAccess;
import de.vsy.chat.module.packet_content_translation.HumanInteractionRequestTranslator;
import de.vsy.chat.module.packet_exception.PacketProcessingException;
import de.vsy.chat.module.packet_processing.ContentProcessor;

public class ContactRelationRequestProcessor
    implements ContentProcessor<ContactRelationRequestDTO> {

  private final StatusDataModelAccess dataModel;

  public ContactRelationRequestProcessor(final StatusDataModelAccess dataModel) {
    this.dataModel = dataModel;
  }

  @Override
  public PacketContent processContent(ContactRelationRequestDTO validatedContent)
      throws PacketProcessingException {
    PacketContent processedData = null;
    final var clientId = this.dataModel.getClientId();
    final var iAmOriginator =
        IdComparator.determineIfOriginator(clientId, validatedContent.getOriginatorId());
    if (!iAmOriginator) {
      if (!validatedContent.getDesiredState()) {
        this.dataModel.removeContactData(
            validatedContent.getContactType(), validatedContent.getContactData());
        this.dataModel.addInformation(
            HumanInteractionRequestTranslator.translate(validatedContent));
      } else {
        this.dataModel.addRequest(validatedContent);
      }
    } else {
      processedData = validatedContent;
    }
    return processedData;
  }
}
