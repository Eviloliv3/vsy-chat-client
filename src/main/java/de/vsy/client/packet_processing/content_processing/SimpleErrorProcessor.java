package de.vsy.client.packet_processing.content_processing;

import static de.vsy.shared_utility.standard_value.StandardIdProvider.STANDARD_CLIENT_ID;

import de.vsy.client.controlling.data_access_interfaces.NotificationDataModelAccess;
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

  private final NotificationDataModelAccess dataModel;
  private final ResultingPacketContentHandler contentHandler;

  /**
   * Instantiates a new simple error handler.
   *
   * @param dataModel the update unit
   */
  public SimpleErrorProcessor(final NotificationDataModelAccess dataModel,
      final ResultingContentHandlingProvider handlerProvider) {
    this.dataModel = dataModel;
    this.contentHandler = handlerProvider.getResultingPacketContentHandler();
  }

  @Override
  public void processContent(ErrorDTO toProcess) {
    final var clientId = this.dataModel.getClientId();

    if (clientId == toProcess.getOriginPacket().getPacketProperties().getSender().getEntityId()) {
      this.dataModel.addNotification(toProcess);

      if (clientId == STANDARD_CLIENT_ID) {
        this.dataModel.resetClient();
      }
    } else {
      this.contentHandler.addRequest(toProcess);
    }
  }
}
