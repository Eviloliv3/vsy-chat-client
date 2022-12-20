package de.vsy.client.packet_processing.processor_provisioning;

import de.vsy.client.controlling.ChatClientController;
import de.vsy.client.packet_processing.ResultingContentHandlingProvider;
import de.vsy.shared_module.packet_processing.processor_provision.ContentBasedProcessorFactory;
import de.vsy.shared_transmission.packet.property.packet_category.PacketCategory;

public interface CategoryProcessorFactoryProvider {

  ContentBasedProcessorFactory getCategoryHandlerFactory(
      PacketCategory category, final ChatClientController dataModel,
      final ResultingContentHandlingProvider handlerProvider);
}
