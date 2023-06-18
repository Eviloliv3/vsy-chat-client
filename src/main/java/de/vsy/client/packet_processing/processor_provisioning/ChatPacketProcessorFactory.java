package de.vsy.client.packet_processing.processor_provisioning;

import static de.vsy.client.packet_processing.processor_provisioning.ContentProcessingConditionProvider.getContentProcessingCondition;

import de.vsy.client.controlling.data_access_interfaces.ChatDataModelAccess;
import de.vsy.client.packet_processing.ClientPacketProcessor;
import de.vsy.client.packet_processing.ResultingContentHandlingProvider;
import de.vsy.client.packet_processing.content_processing.SimpleMessageProcessor;
import de.vsy.shared_module.packet_processing.PacketProcessor;
import de.vsy.shared_module.packet_processing.ProcessingConditionType;
import de.vsy.shared_module.packet_processing.processor_provision.ContentBasedProcessorFactory;
import de.vsy.shared_module.packet_validation.content_validation.chat.TextMessageValidator;
import de.vsy.shared_transmission.packet.content.PacketContent;
import de.vsy.shared_transmission.packet.content.chat.ChatContent;

/**
 * Factory for creating chat category PacketHandlers.
 *
 * <p>Frederic Heath
 */
public class ChatPacketProcessorFactory implements ContentBasedProcessorFactory {

  private final ChatDataModelAccess dataManager;
  private final ResultingContentHandlingProvider handlerProvider;

  /**
   * Instantiates a new chat handler.
   *
   * @param dataManager the dataManagement manager
   */
  public ChatPacketProcessorFactory(final ChatDataModelAccess dataManager,
      final ResultingContentHandlingProvider handlerProvider) {
    this.dataManager = dataManager;
    this.handlerProvider = handlerProvider;
  }

  @Override
  public PacketProcessor createTypeProcessor(Class<? extends PacketContent> contentType) {
    final var chatType = ChatContent.valueOf(contentType.getSimpleName());

    if (chatType == ChatContent.TextMessageDTO) {
      return new ClientPacketProcessor<>(
          getContentProcessingCondition(ProcessingConditionType.AUTHENTICATED, this.dataManager),
          new TextMessageValidator(),
          new SimpleMessageProcessor(this.dataManager, this.handlerProvider));
    }
    return null;
  }
}
