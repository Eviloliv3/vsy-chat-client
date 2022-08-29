/*
 *
 */
package de.vsy.chat.client.packet_processing.processor_provisioning;

import de.vsy.chat.client.controlling.data_access_interfaces.StatusDataModelAccess;
import de.vsy.chat.client.packet_processing.ClientPacketProcessor;
import de.vsy.chat.client.packet_processing.content_processing.ClientStatusProcessor;
import de.vsy.chat.client.packet_processing.content_processing.ContactStatusChangeProcessor;
import de.vsy.chat.client.packet_processing.content_processing.MessengerSetupProcessor;
import de.vsy.chat.client.packet_processing.content_processing.MessengerTearDownProcessor;
import de.vsy.chat.transmission.packet.content.PacketContent;
import de.vsy.chat.transmission.packet.content.status.StatusContent;
import de.vsy.chat.module.packet_processing.PacketProcessor;
import de.vsy.chat.module.packet_processing.ProcessingConditionType;
import de.vsy.chat.module.packet_processing.processor_provision.PacketCategoryProcessorFactory;
import de.vsy.chat.module.packet_validation.content_validation.status.ClientStatusValidator;
import de.vsy.chat.module.packet_validation.content_validation.status.ContactStatusValidator;
import de.vsy.chat.module.packet_validation.content_validation.status.MessengerSetupValidator;
import de.vsy.chat.module.packet_validation.content_validation.status.MessengerTearDownValidator;

public class StatusPacketProcessorFactory implements PacketCategoryProcessorFactory {

  private final StatusDataModelAccess dataManager;

  /**
   * Instantiates a new setup handler.
   *
   * @param dataManager the dataManagement manager
   */
  public StatusPacketProcessorFactory(final StatusDataModelAccess dataManager) {
    this.dataManager = dataManager;
  }

  @Override
  public PacketProcessor createTypeProcessor(Class<? extends PacketContent> contentType) {
    final var contentType = StatusContent.valueOf(contentType.getSimpleName());

    switch (contentType) {
      case ClientStatusChangeDTO:
        return new ClientPacketProcessor<>(
            ContentProcessingConditionProvider.getContentProcessingCondition(
                ProcessingConditionType.AUTHENTICATED, this.dataManager),
            new ClientStatusValidator(),
            new ClientStatusProcessor(this.dataManager));
      case ContactMessengerStatusDTO:
        return new ClientPacketProcessor<>(
            ContentProcessingConditionProvider.getContentProcessingCondition(
                ProcessingConditionType.AUTHENTICATED, this.dataManager),
            new ContactStatusValidator(),
            new ContactStatusChangeProcessor(this.dataManager));
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
