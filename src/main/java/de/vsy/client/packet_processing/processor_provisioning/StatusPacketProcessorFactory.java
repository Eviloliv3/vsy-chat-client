/*
 *
 */
package de.vsy.client.packet_processing.processor_provisioning;

import de.vsy.client.controlling.data_access_interfaces.StatusDataModelAccess;
import de.vsy.client.packet_processing.ClientPacketProcessor;
import de.vsy.client.packet_processing.ResultingContentHandlingProvider;
import de.vsy.client.packet_processing.content_processing.ClientStatusProcessor;
import de.vsy.client.packet_processing.content_processing.ContactStatusChangeProcessor;
import de.vsy.client.packet_processing.content_processing.MessengerSetupProcessor;
import de.vsy.client.packet_processing.content_processing.MessengerTearDownProcessor;
import de.vsy.shared_module.packet_processing.PacketProcessor;
import de.vsy.shared_module.packet_processing.ProcessingConditionType;
import de.vsy.shared_module.packet_processing.processor_provision.ContentBasedProcessorFactory;
import de.vsy.shared_module.packet_validation.content_validation.status.ClientStatusValidator;
import de.vsy.shared_module.packet_validation.content_validation.status.ContactStatusValidator;
import de.vsy.shared_module.packet_validation.content_validation.status.MessengerSetupValidator;
import de.vsy.shared_module.packet_validation.content_validation.status.MessengerTearDownValidator;
import de.vsy.shared_transmission.packet.content.PacketContent;
import de.vsy.shared_transmission.packet.content.status.StatusContent;

public class StatusPacketProcessorFactory implements ContentBasedProcessorFactory {

  private final StatusDataModelAccess dataManager;
  private final ResultingContentHandlingProvider handlerProvider;

  /**
   * Instantiates a new setup handler.
   *
   * @param dataManager the dataManagement manager
   */
  public StatusPacketProcessorFactory(final StatusDataModelAccess dataManager,
      final ResultingContentHandlingProvider handlerProvider) {
    this.dataManager = dataManager;
    this.handlerProvider = handlerProvider;
  }

  @Override
  public PacketProcessor createTypeProcessor(Class<? extends PacketContent> contentType) {
    final var statusType = StatusContent.valueOf(contentType.getSimpleName());

    switch (statusType) {
      case ClientStatusChangeDTO:
        return new ClientPacketProcessor<>(
            ContentProcessingConditionProvider.getContentProcessingCondition(
                ProcessingConditionType.AUTHENTICATED, this.dataManager),
            new ClientStatusValidator(),
            new ClientStatusProcessor(this.dataManager, this.handlerProvider));
      case ContactMessengerStatusDTO:
        return new ClientPacketProcessor<>(
            ContentProcessingConditionProvider.getContentProcessingCondition(
                ProcessingConditionType.AUTHENTICATED, this.dataManager),
            new ContactStatusValidator(),
            new ContactStatusChangeProcessor(this.dataManager, this.handlerProvider));
      case MessengerSetupDTO:
        return new ClientPacketProcessor<>(
            ContentProcessingConditionProvider.getContentProcessingCondition(
                ProcessingConditionType.AUTHENTICATED, this.dataManager),
            new MessengerSetupValidator(),
            new MessengerSetupProcessor(this.dataManager));
      case MessengerTearDownDTO:
        return new ClientPacketProcessor<>(
            ContentProcessingConditionProvider.getContentProcessingCondition(
                ProcessingConditionType.AUTHENTICATED, this.dataManager),
            new MessengerTearDownValidator(),
            new MessengerTearDownProcessor(this.dataManager));
      default:
        break;
    }
    return null;
  }
}
