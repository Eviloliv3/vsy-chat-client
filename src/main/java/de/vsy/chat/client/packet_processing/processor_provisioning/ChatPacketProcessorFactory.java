/*
 *
 */
package de.vsy.chat.client.packet_processing.processor_provisioning;

import de.vsy.chat.transmission.packet.content.PacketContent;
import de.vsy.chat.transmission.packet.content.chat.ChatContent;

import static de.vsy.chat.client.packet_processing.processor_provisioning.ContentProcessingConditionProvider.getContentProcessingCondition;

import de.vsy.chat.client.controlling.data_access_interfaces.ChatDataModelAccess;
import de.vsy.chat.client.packet_processing.ClientPacketProcessor;
import de.vsy.chat.client.packet_processing.content_processing.SimpleMessageProcessor;
import de.vsy.chat.module.packet_processing.PacketProcessor;
import de.vsy.chat.module.packet_processing.ProcessingConditionType;
import de.vsy.chat.module.packet_processing.processor_provision.PacketCategoryProcessorFactory;
import de.vsy.chat.module.packet_validation.content_validation.chat.TextMessageValidator;

/**
 * Factory for creating chat category PacketHandlers.
 *
 * <p>Frederic Heath
 */
public class ChatPacketProcessorFactory implements PacketCategoryProcessorFactory {

  private final ChatDataModelAccess dataManager;

  /**
   * Instantiates a new chat handler.
   *
   * @param dataManager the dataManagement manager
   */
  public ChatPacketProcessorFactory(final ChatDataModelAccess dataManager) {
    this.dataManager = dataManager;
  }

  @Override
  public PacketProcessor createTypeProcessor(Class<? extends PacketContent> contentType) {
    final var contentType = ChatContent.valueOf(contentType.getSimpleName());

    if (contentType == ChatContent.TextMessageDTO) {
      return new ClientPacketProcessor<>(
          getContentProcessingCondition(ProcessingConditionType.AUTHENTICATED, this.dataManager),
          new TextMessageValidator(),
          new SimpleMessageProcessor(this.dataManager));
    }
    return null;
  }
}
