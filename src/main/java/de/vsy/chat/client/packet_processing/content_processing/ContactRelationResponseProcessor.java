package de.vsy.chat.client.packet_processing.content_processing;

import de.vsy.chat.transmission.packet.content.PacketContent;
import de.vsy.chat.transmission.packet.content.relation.ContactRelationResponseDTO;
import de.vsy.chat.utility.id_manipulation.IdComparator;
import de.vsy.chat.client.controlling.data_access_interfaces.StatusDataModelAccess;
import de.vsy.chat.module.packet_content_translation.HumanInteractionRequestTranslator;
import de.vsy.chat.module.packet_exception.PacketProcessingException;
import de.vsy.chat.module.packet_processing.ContentProcessor;

public class ContactRelationResponseProcessor
    implements ContentProcessor<ContactRelationResponseDTO> {

  private final StatusDataModelAccess dataModel;
  public ContactRelationResponseProcessor(final StatusDataModelAccess dataModel) {
    this.dataModel = dataModel;
  }

  @Override
  public PacketContent processContent(ContactRelationResponseDTO toProcess)
      throws PacketProcessingException {
    PacketContent processedData = null;
    final var clientId = this.dataModel.getClientId();
    final var iAmOriginator =
        IdComparator.determineIfOriginator(clientId, toProcess.getOriginatorId());

    if (toProcess.getDesiredState() && toProcess.getDecision()) {
      this.dataModel.addContactData(toProcess.getContactType(), toProcess.getContactData(), null);
    }

    if (!iAmOriginator) {
      this.dataModel.addInformation(HumanInteractionRequestTranslator.translate(toProcess));
    } else {
      processedData = toProcess;
    }
    return processedData;
  }
}
