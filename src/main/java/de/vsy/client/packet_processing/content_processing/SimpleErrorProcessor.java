/*
 *
 */
package de.vsy.client.packet_processing.content_processing;

import de.vsy.client.controlling.data_access_interfaces.EssentialDataModelAccess;
import de.vsy.client.packet_processing.ResultingContentHandlingProvider;
import de.vsy.client.packet_processing.ResultingPacketContentHandler;
import de.vsy.shared_module.packet_processing.ContentProcessor;
import de.vsy.shared_transmission.packet.content.error.ErrorDTO;

/**
 * PacketProcessor for error type Packet.
 *
 * <p>Frederic Heath
 */
public class SimpleErrorProcessor implements ContentProcessor<ErrorDTO> {

  private final EssentialDataModelAccess dataModel;
  private final ResultingPacketContentHandler contentHandler;

  /**
   * Instantiates a new simple error handler.
   *
   * @param dataModel the update unit
   */
  public SimpleErrorProcessor(final EssentialDataModelAccess dataModel,
      final ResultingContentHandlingProvider handlerProvider) {
    this.dataModel = dataModel;
    this.contentHandler = handlerProvider.getResultingPacketContentHandler();
  }

  @Override
  public void processContent(ErrorDTO toProcess) {
    if(toProcess.getOriginPacket().getPacketProperties().getSender().getEntityId() == this.dataModel.getClientId()) {
      this.dataModel.addNotification(toProcess);
    }else {
      this.contentHandler.addRequest(toProcess);
    }
  }
}
