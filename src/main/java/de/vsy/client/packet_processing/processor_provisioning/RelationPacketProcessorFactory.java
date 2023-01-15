
package de.vsy.client.packet_processing.processor_provisioning;

import de.vsy.client.controlling.data_access_interfaces.StatusDataModelAccess;
import de.vsy.client.packet_processing.ClientPacketProcessor;
import de.vsy.client.packet_processing.ResultingContentHandlingProvider;
import de.vsy.client.packet_processing.content_processing.ContactRelationRequestProcessor;
import de.vsy.client.packet_processing.content_processing.ContactRelationResponseProcessor;
import de.vsy.shared_module.packet_processing.PacketProcessor;
import de.vsy.shared_module.packet_processing.ProcessingConditionType;
import de.vsy.shared_module.packet_processing.processor_provision.ContentBasedProcessorFactory;
import de.vsy.shared_module.packet_validation.content_validation.relation.ContactRelationRequestValidator;
import de.vsy.shared_module.packet_validation.content_validation.relation.ContactRelationResponseValidator;
import de.vsy.shared_transmission.packet.content.PacketContent;
import de.vsy.shared_transmission.packet.content.relation.RelationContent;

public class RelationPacketProcessorFactory implements ContentBasedProcessorFactory {

  private final StatusDataModelAccess dataManager;
  private final ResultingContentHandlingProvider handlerProvider;

  /**
   * Instantiates a new update handler.
   *
   * @param dataManager the dataManagement manager
   */
  public RelationPacketProcessorFactory(final StatusDataModelAccess dataManager,
      final ResultingContentHandlingProvider handlerProvider) {
    this.dataManager = dataManager;
    this.handlerProvider = handlerProvider;
  }

  @Override
  public PacketProcessor createTypeProcessor(Class<? extends PacketContent> contentType) {
    final var relationType = RelationContent.valueOf(contentType.getSimpleName());

    return switch (relationType) {
      case ContactRelationRequestDTO -> new ClientPacketProcessor<>(
          ContentProcessingConditionProvider.getContentProcessingCondition(
              ProcessingConditionType.AUTHENTICATED, this.dataManager),
          new ContactRelationRequestValidator(),
          new ContactRelationRequestProcessor(this.dataManager, this.handlerProvider));
      case ContactRelationResponseDTO -> new ClientPacketProcessor<>(
          ContentProcessingConditionProvider.getContentProcessingCondition(
              ProcessingConditionType.AUTHENTICATED, this.dataManager),
          new ContactRelationResponseValidator(),
          new ContactRelationResponseProcessor(this.dataManager, this.handlerProvider));
    };
  }
}
