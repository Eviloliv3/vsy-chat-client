package de.vsy.client.packet_processing.processor_provisioning;

import de.vsy.client.data_model.InputController;
import de.vsy.client.packet_processing.ResultingContentHandlingProvider;
import de.vsy.shared_module.packet_processing.processor_provision.ContentBasedProcessorFactory;
import de.vsy.shared_transmission.packet.property.packet_category.PacketCategory;

public interface CategoryProcessorFactoryProvider {

  ContentBasedProcessorFactory getCategoryHandlerFactory(
      PacketCategory category, final InputController dataModel,
      final ResultingContentHandlingProvider handlerProvider);
}
