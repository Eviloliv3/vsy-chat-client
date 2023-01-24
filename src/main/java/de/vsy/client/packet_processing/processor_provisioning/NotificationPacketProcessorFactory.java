package de.vsy.client.packet_processing.processor_provisioning;

import de.vsy.client.controlling.data_access_interfaces.NotificationDataModelAccess;
import de.vsy.client.packet_processing.ClientPacketProcessor;
import de.vsy.client.packet_processing.ResultingContentHandlingProvider;
import de.vsy.client.packet_processing.content_processing.NotificationProcessor;
import de.vsy.shared_module.packet_processing.PacketProcessor;
import de.vsy.shared_module.packet_processing.ProcessingCondition;
import de.vsy.shared_module.packet_processing.processor_provision.ContentBasedProcessorFactory;
import de.vsy.shared_module.packet_validation.content_validation.error.NotificationContentValidator;
import de.vsy.shared_transmission.packet.content.PacketContent;
import de.vsy.shared_transmission.packet.content.notification.NotificationContent;

public class NotificationPacketProcessorFactory implements ContentBasedProcessorFactory {

  private final NotificationDataModelAccess dataManager;
  private final ResultingContentHandlingProvider handlerProvider;

  /**
   * Instantiates a new notification handler.
   *
   * @param dataManager the dataManagement manager
   */
  public NotificationPacketProcessorFactory(final NotificationDataModelAccess dataManager,
      final ResultingContentHandlingProvider handlerProvider) {
    this.dataManager = dataManager;
    this.handlerProvider = handlerProvider;
  }

  @Override
  public PacketProcessor createTypeProcessor(Class<? extends PacketContent> contentType) {
    final var notificationType = NotificationContent.valueOf(contentType.getSimpleName());

    if (notificationType.equals(NotificationContent.ErrorDTO) || notificationType.equals(NotificationContent.SimpleInformationDTO)) {
      return new ClientPacketProcessor<>(
          getSimpleErrorProcessingCondition(),
          new NotificationContentValidator(),
          new NotificationProcessor(this.dataManager, this.handlerProvider));
    }
    return null;
  }

  private ProcessingCondition getSimpleErrorProcessingCondition() {
    return new ProcessingCondition() {
      @Override
      public boolean checkCondition() {
        return true;
      }

      @Override
      public String getErrorMessage() {
        return "This notification message should not occur, because checkCondition always returns true.";
      }
    };
  }
}
