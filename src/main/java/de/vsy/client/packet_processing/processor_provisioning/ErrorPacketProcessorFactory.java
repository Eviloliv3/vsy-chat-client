/*
 *
 */
package de.vsy.client.packet_processing.processor_provisioning;

import de.vsy.client.controlling.data_access_interfaces.EssentialDataModelAccess;
import de.vsy.client.packet_processing.ClientPacketProcessor;
import de.vsy.client.packet_processing.ResultingContentHandlingProvider;
import de.vsy.client.packet_processing.content_processing.SimpleErrorProcessor;
import de.vsy.shared_module.packet_processing.PacketProcessor;
import de.vsy.shared_module.packet_processing.ProcessingCondition;
import de.vsy.shared_module.packet_processing.processor_provision.ContentBasedProcessorFactory;
import de.vsy.shared_module.packet_validation.content_validation.error.ErrorContentValidator;
import de.vsy.shared_transmission.packet.content.PacketContent;
import de.vsy.shared_transmission.packet.content.error.ErrorContent;

public class ErrorPacketProcessorFactory implements ContentBasedProcessorFactory {

  private final EssentialDataModelAccess dataManager;
  private final ResultingContentHandlingProvider handlerProvider;

  /**
   * Instantiates a new error handler.
   *
   * @param dataManager the dataManagement manager
   */
  public ErrorPacketProcessorFactory(final EssentialDataModelAccess dataManager,
      final ResultingContentHandlingProvider handlerProvider) {
    this.dataManager = dataManager;
    this.handlerProvider = handlerProvider;
  }

  @Override
  public PacketProcessor createTypeProcessor(Class<? extends PacketContent> contentType) {
    final var errorType = ErrorContent.valueOf(contentType.getSimpleName());

    if (errorType.equals(ErrorContent.ErrorDTO)) {
      return new ClientPacketProcessor<>(
          getSimpleErrorProcessingCondition(),
          new ErrorContentValidator(),
          new SimpleErrorProcessor(this.dataManager, this.handlerProvider));
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
