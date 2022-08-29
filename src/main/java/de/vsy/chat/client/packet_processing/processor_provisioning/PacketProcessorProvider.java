/*
 *
 */
package de.vsy.chat.client.packet_processing.processor_provisioning;

import de.vsy.chat.transmission.packet.content.PacketContent;
import de.vsy.chat.transmission.packet.property.packet_identifier.ContentIdentifier;
import de.vsy.chat.client.data_model.DataInputController;
import de.vsy.chat.module.packet_processing.PacketProcessor;
import de.vsy.chat.module.packet_processing.processor_provision.PacketCategoryProcessorProvider;
import de.vsy.chat.module.packet_processing.processor_provision.PacketContentProcessorProvider;

/**
 * A factory for creating PacketCategoryFactories.
 *
 * <p>Frederic Heath
 */
public class PacketProcessorProvider {

  private final DataInputController dataModel;
  private final PacketContentProcessorProvider contentProcessorProvider;
  private final CategoryProcessorFactoryProvider processorFactoryProvider;

  /**
   * Instantiates a new PacketHandler factory.
   *
   * @param dataModel the dataManagement model
   */
  public PacketProcessorProvider(
      final DataInputController dataModel,
      final CategoryProcessorFactoryProvider processorFactoryProvider) {
    contentProcessorProvider = new PacketContentProcessorProvider();
    this.dataModel = dataModel;
    this.processorFactoryProvider = processorFactoryProvider;
  }

  public PacketProcessor getProcessor(
      ContentIdentifier identifier, Class<? extends PacketContent> contentType) {
    var categoryProcessing =
        contentProcessorProvider.getProcessor(identifier.getPacketCategory(), contentType);

    if (categoryProcessing == null) {
      var factory =
          new PacketCategoryProcessorProvider(
              this.processorFactoryProvider.getCategoryHandlerFactory(
                  identifier.getPacketCategory(), this.dataModel));
      this.contentProcessorProvider.registerTypeProcessingProvider(
          identifier.getPacketCategory(), factory);
      categoryProcessing = factory.getProcessor(contentType);
    }
    return categoryProcessing;
  }
}
