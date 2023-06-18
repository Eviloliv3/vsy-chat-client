package de.vsy.client.packet_processing.processor_provisioning;

import de.vsy.shared_module.packet_processing.processor_provision.ContentBasedProcessorFactory;
import de.vsy.shared_transmission.packet.property.packet_category.PacketCategory;

public interface CategoryProcessorFactoryProvider {

  /**
   * Returns a factory providing PacketProcessors capable of processing Packets of the specified
   * PacketCategory.
   *
   * @param category the PacketCategory
   * @return a ContentBasedProcessorFactory, or null if no factory can be provided for the specified
   * PacketCategory
   */
  ContentBasedProcessorFactory getCategoryHandlerFactory(PacketCategory category);
}
