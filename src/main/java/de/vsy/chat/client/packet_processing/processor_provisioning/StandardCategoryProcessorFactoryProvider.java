package de.vsy.chat.client.packet_processing.processor_provisioning;

import de.vsy.chat.client.data_model.DataInputController;
import de.vsy.chat.module.packet_processing.processor_provision.PacketCategoryProcessorFactory;
import de.vsy.chat.transmission.packet.property.packet_category.PacketCategory;

public class StandardCategoryProcessorFactoryProvider implements CategoryProcessorFactoryProvider {

  @Override
  public PacketCategoryProcessorFactory getCategoryHandlerFactory(
      final PacketCategory category, final DataInputController dataModel) {
    PacketCategoryProcessorFactory categoryFactory = null;

    switch (category) {
      case AUTHENTICATION:
        categoryFactory = new AuthenticationPacketProcessorFactory(dataModel);
        break;
      case STATUS:
        categoryFactory = new StatusPacketProcessorFactory(dataModel);
        break;
      case RELATION:
        categoryFactory = new RelationPacketProcessorFactory(dataModel);
        break;
      case CHAT:
        categoryFactory = new ChatPacketProcessorFactory(dataModel);
        break;
      case ERROR:
        categoryFactory = new ErrorPacketProcessorFactory(dataModel);
        break;
      default:
        break;
    }
    return categoryFactory;
  }
}
