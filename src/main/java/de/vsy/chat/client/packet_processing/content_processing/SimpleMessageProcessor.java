/*
 *
 */
package de.vsy.chat.client.packet_processing.content_processing;

import de.vsy.chat.transmission.packet.content.PacketContent;
import de.vsy.chat.transmission.packet.content.chat.TextMessageDTO;
import de.vsy.chat.utility.id_manipulation.IdComparator;
import de.vsy.chat.client.controlling.data_access_interfaces.ChatDataModelAccess;
import de.vsy.chat.module.packet_exception.PacketProcessingException;
import de.vsy.chat.module.packet_processing.ContentProcessor;

/**
 * PacketProcessor for text message type Packet.
 *
 * <p>Frederic Heath
 */
public class SimpleMessageProcessor implements ContentProcessor<TextMessageDTO> {

  private final ChatDataModelAccess dataModel;

  /**
   * Instantiates a new simple message handler.
   *
   * @param dataModel the update unit
   */
  public SimpleMessageProcessor(final ChatDataModelAccess dataModel) {
    this.dataModel = dataModel;
  }

  @Override
  public PacketContent processContent(TextMessageDTO toProcess) throws PacketProcessingException {
    PacketContent processedData = null;
    final var messageReceived = toProcess.getReceptionState();
    final var iAmInitiator =
        IdComparator.determineIfOriginator(
            this.dataModel.getClientId(), toProcess.getOriginatorId());
    if (!iAmInitiator || messageReceived) {
      this.dataModel.addMessage(toProcess);
    }

    if (!iAmInitiator) {
      processedData = TextMessageDTO.setReceptionState(toProcess);
    } else if (!messageReceived) {
      processedData = toProcess;
    }
    return processedData;
  }
}
