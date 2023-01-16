package de.vsy.client.packet_processing.processor_provisioning;

import de.vsy.client.controlling.ChatClientController;
import de.vsy.client.packet_processing.ResultingContentHandlingProvider;
import de.vsy.shared_module.packet_processing.processor_provision.ContentBasedProcessorFactory;
import de.vsy.shared_transmission.packet.property.packet_category.PacketCategory;

public class StandardProcessorFactoryProvider implements CategoryProcessorFactoryProvider {

  private final ChatClientController dataModel;
  private final ResultingContentHandlingProvider handlerProvider;

  public StandardProcessorFactoryProvider(final ChatClientController dataModel,
      final ResultingContentHandlingProvider handlerProvider){
    this.dataModel = dataModel;
    this.handlerProvider = handlerProvider;
  }

  @Override
  public ContentBasedProcessorFactory getCategoryHandlerFactory(final PacketCategory category) {
    return switch (category) {
      case AUTHENTICATION -> new AuthenticationPacketProcessorFactory(this.dataModel, this.handlerProvider);
      case STATUS -> new StatusPacketProcessorFactory(this.dataModel, this.handlerProvider);
      case RELATION -> new RelationPacketProcessorFactory(this.dataModel, this.handlerProvider);
      case CHAT -> new ChatPacketProcessorFactory(this.dataModel, this.handlerProvider);
      case NOTIFICATION -> new ErrorPacketProcessorFactory(this.dataModel, this.handlerProvider);
    };
  }
}
