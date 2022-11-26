/*
 *
 */
package de.vsy.client.packet_processing.content_processing;

import de.vsy.client.controlling.data_access_interfaces.ChatDataModelAccess;
import de.vsy.client.packet_processing.ResultingContentHandlingProvider;
import de.vsy.client.packet_processing.ResultingPacketContentHandler;
import de.vsy.shared_module.packet_processing.ContentProcessor;
import de.vsy.shared_transmission.packet.content.PacketContent;
import de.vsy.shared_transmission.packet.content.chat.TextMessageDTO;
import de.vsy.shared_utility.id_manipulation.IdComparator;

/**
 * PacketProcessor for text message type Packet.
 *
 * <p>Frederic Heath
 */
public class SimpleMessageProcessor implements ContentProcessor<TextMessageDTO> {

  private final ChatDataModelAccess dataModel;
  private final ResultingPacketContentHandler contentHandler;

  /**
   * Instantiates a new simple message handler.
   *
   * @param dataModel the update unit
   */
  public SimpleMessageProcessor(final ChatDataModelAccess dataModel,
      final ResultingContentHandlingProvider handlerProvider) {
    this.dataModel = dataModel;
    this.contentHandler = handlerProvider.getResultingPacketContentHandler();
  }

  @Override
  public void processContent(TextMessageDTO toProcess) {
    PacketContent processedData = null;
    final var messageReceived = toProcess.getReceptionState();
    final var iAmInitiator =
        IdComparator.determineIfOriginator(
            this.dataModel.getClientId(), toProcess.getOriginatorId());
    if (!iAmInitiator || messageReceived) {
      this.dataModel.addMessage(toProcess);
    }

    if (!iAmInitiator) {
      processedData = toProcess.setReceptionState();
    } else if (!messageReceived) {
      processedData = toProcess;
    }
    this.contentHandler.addRequest(processedData);
  }
}