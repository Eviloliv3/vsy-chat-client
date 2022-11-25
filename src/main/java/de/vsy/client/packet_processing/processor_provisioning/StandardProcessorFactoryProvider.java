package de.vsy.client.packet_processing.processor_provisioning;

import de.vsy.client.data_model.DataInputController;
import de.vsy.shared_module.packet_processing.processor_provision.ContentBasedProcessorFactory;
import de.vsy.shared_transmission.packet.property.packet_category.PacketCategory;

public class StandardProcessorFactoryProvider implements CategoryProcessorFactoryProvider {

  @Override
  public ContentBasedProcessorFactory getCategoryHandlerFactory(
      final PacketCategory category, final DataInputController dataModel) {
    ContentBasedProcessorFactory categoryFactory = null;

    switch (category) {
      case AUTHENTICATION -> categoryFactory = new AuthenticationPacketProcessorFactory(dataModel);
      case STATUS -> categoryFactory = new StatusPacketProcessorFactory(dataModel);
      case RELATION -> categoryFactory = new RelationPacketProcessorFactory(dataModel);
      case CHAT -> categoryFactory = new ChatPacketProcessorFactory(dataModel);
      case ERROR -> categoryFactory = new ErrorPacketProcessorFactory(dataModel);
      default -> {
      }
    }
    return categoryFactory;
  }
}
