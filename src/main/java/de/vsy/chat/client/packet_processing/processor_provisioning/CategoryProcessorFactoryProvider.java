package de.vsy.chat.client.packet_processing.processor_provisioning;

import de.vsy.chat.client.data_model.DataInputController;
import de.vsy.chat.module.packet_processing.processor_provision.PacketCategoryProcessorFactory;
import de.vsy.chat.transmission.packet.property.packet_category.PacketCategory;

public interface CategoryProcessorFactoryProvider {

  PacketCategoryProcessorFactory getCategoryHandlerFactory(
      PacketCategory category, final DataInputController dataModel);
}
