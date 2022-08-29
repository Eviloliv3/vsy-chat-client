/*
 *
 */
package de.vsy.chat.client.packet_processing.processor_provisioning;

import de.vsy.chat.transmission.packet.content.PacketContent;
import de.vsy.chat.client.controlling.data_access_interfaces.EssentialDataModelAccess;
import de.vsy.chat.client.packet_processing.ClientPacketProcessor;
import de.vsy.chat.client.packet_processing.content_processing.SimpleErrorProcessor;
import de.vsy.chat.module.packet_processing.PacketProcessor;
import de.vsy.chat.module.packet_processing.ProcessingCondition;
import de.vsy.chat.module.packet_processing.processor_provision.PacketCategoryProcessorFactory;
import de.vsy.chat.module.packet_validation.content_validation.error.ErrorContentValidator;
import de.vsy.chat.transmission.packet.content.error.ErrorContent;

public class ErrorPacketProcessorFactory implements PacketCategoryProcessorFactory {

  private final EssentialDataModelAccess dataManager;

  /**
   * Instantiates a new error handler.
   *
   * @param dataManager the dataManagement manager
   */
  public ErrorPacketProcessorFactory(final EssentialDataModelAccess dataManager) {
    this.dataManager = dataManager;
  }

  @Override
  public PacketProcessor createTypeProcessor(Class<? extends PacketContent> contentType) {
    final var contentType = ErrorContent.valueOf(contentType.getSimpleName());

    if (contentType.equals(ErrorContent.ErrorDTO)) {
      return new ClientPacketProcessor<>(
          getSimpleErrorProcessingCondition(),
          new ErrorContentValidator(),
          new SimpleErrorProcessor(this.dataManager));
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
        return "Diese Fehlernachricht sollte nie abgefragt werden.";
      }
    };
  }
}
