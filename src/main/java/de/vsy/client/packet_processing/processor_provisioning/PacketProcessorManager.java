
package de.vsy.client.packet_processing.processor_provisioning;

import de.vsy.client.controlling.ChatClientController;
import de.vsy.client.packet_processing.ResultingContentHandlingProvider;
import de.vsy.shared_module.packet_processing.PacketProcessor;
import de.vsy.shared_module.packet_processing.processor_provision.ContentBasedPacketProcessorProvider;
import de.vsy.shared_module.packet_processing.processor_provision.PacketProcessorProvider;
import de.vsy.shared_transmission.packet.content.PacketContent;
import de.vsy.shared_transmission.packet.property.packet_identifier.ContentIdentifier;
import java.util.Optional;

/**
 * A factory for creating PacketCategoryFactories.
 *
 * <p>Frederic Heath
 */
public class PacketProcessorManager {

  private final PacketProcessorProvider contentHandlerProvider;
  private final CategoryProcessorFactoryProvider processorFactoryProvider;

  public PacketProcessorManager(final CategoryProcessorFactoryProvider processorFactoryProvider) {
    this.contentHandlerProvider = new PacketProcessorProvider();
    this.processorFactoryProvider = processorFactoryProvider;
  }

  public Optional<PacketProcessor> getProcessor(
      ContentIdentifier identifier, Class<? extends PacketContent> contentType) {
    Optional<PacketProcessor> categoryProcessing =
        this.contentHandlerProvider.getProcessor(identifier.getPacketCategory(), contentType);

    if (categoryProcessing.isEmpty()) {
      var factory =
          new ContentBasedPacketProcessorProvider(
              this.processorFactoryProvider.getCategoryHandlerFactory(identifier.getPacketCategory()));
      this.contentHandlerProvider.registerTypeProcessingProvider(
          identifier.getPacketCategory(), factory);
      categoryProcessing = factory.getProcessor(contentType);
    }
    return categoryProcessing;
  }
}
