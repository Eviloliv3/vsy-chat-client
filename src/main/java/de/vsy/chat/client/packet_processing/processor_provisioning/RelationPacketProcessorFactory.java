/*
 *
 */
package de.vsy.chat.client.packet_processing.processor_provisioning;

import de.vsy.chat.client.controlling.data_access_interfaces.StatusDataModelAccess;
import de.vsy.chat.client.packet_processing.ClientPacketProcessor;
import de.vsy.chat.client.packet_processing.content_processing.ContactRelationRequestProcessor;
import de.vsy.chat.client.packet_processing.content_processing.ContactRelationResponseProcessor;
import de.vsy.chat.transmission.packet.content.PacketContent;
import de.vsy.chat.transmission.packet.content.relation.RelationContent;
import de.vsy.chat.module.packet_processing.PacketProcessor;
import de.vsy.chat.module.packet_processing.ProcessingConditionType;
import de.vsy.chat.module.packet_processing.processor_provision.PacketCategoryProcessorFactory;
import de.vsy.chat.module.packet_validation.content_validation.relation.ContactRelationRequestValidator;
import de.vsy.chat.module.packet_validation.content_validation.relation.ContactRelationResponseValidator;

public class RelationPacketProcessorFactory implements PacketCategoryProcessorFactory {

  private final StatusDataModelAccess dataManager;

  /**
   * Instantiates a new update handler.
   *
   * @param dataManager the dataManagement manager
   */
  public RelationPacketProcessorFactory(final StatusDataModelAccess dataManager) {
    this.dataManager = dataManager;
  }

  @Override
  public PacketProcessor createTypeProcessor(Class<? extends PacketContent> contentType) {
    final var contentType = RelationContent.valueOf(contentType.getSimpleName());

    switch (contentType) {
      case ContactRelationRequestDTO:
        return new ClientPacketProcessor<>(
            ContentProcessingConditionProvider.getContentProcessingCondition(
                ProcessingConditionType.NOT_AUTHENTICATED, this.dataManager),
            new ContactRelationRequestValidator(),
            new ContactRelationRequestProcessor(this.dataManager));
      case ContactRelationResponseDTO:
        return new ClientPacketProcessor<>(
            ContentProcessingConditionProvider.getContentProcessingCondition(
                ProcessingConditionType.AUTHENTICATED, this.dataManager),
            new ContactRelationResponseValidator(),
            new ContactRelationResponseProcessor(this.dataManager));
      default:
        break;
    }
    return null;
  }
}
