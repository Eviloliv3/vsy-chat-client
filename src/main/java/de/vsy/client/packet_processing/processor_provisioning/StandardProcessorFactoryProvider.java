package de.vsy.client.packet_processing.processor_provisioning;

import de.vsy.client.controlling.ChatClientController;
import de.vsy.client.packet_processing.ResultingContentHandlingProvider;
import de.vsy.shared_module.packet_processing.processor_provision.ContentBasedProcessorFactory;
import de.vsy.shared_transmission.packet.property.packet_category.PacketCategory;

public class StandardProcessorFactoryProvider implements CategoryProcessorFactoryProvider {

  @Override
  public ContentBasedProcessorFactory getCategoryHandlerFactory(
      final PacketCategory category, final ChatClientController dataModel,
      final ResultingContentHandlingProvider handlerProvider) {
    ContentBasedProcessorFactory categoryFactory = null;

    switch (category) {
      case AUTHENTICATION ->
          categoryFactory = new AuthenticationPacketProcessorFactory(dataModel, handlerProvider);
      case STATUS -> categoryFactory = new StatusPacketProcessorFactory(dataModel, handlerProvider);
      case RELATION ->
          categoryFactory = new RelationPacketProcessorFactory(dataModel, handlerProvider);
      case CHAT -> categoryFactory = new ChatPacketProcessorFactory(dataModel, handlerProvider);
      case ERROR -> categoryFactory = new ErrorPacketProcessorFactory(dataModel, handlerProvider);
    }
    return categoryFactory;
  }
}
