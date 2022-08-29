/*
 *
 */
package de.vsy.chat.client.packet_processing.content_processing;

import de.vsy.chat.transmission.packet.content.PacketContent;
import de.vsy.chat.transmission.packet.content.authentication.ReconnectResponseDTO;
import de.vsy.chat.client.controlling.data_access_interfaces.AuthenticationDataModelAccess;
import de.vsy.chat.module.packet_exception.PacketProcessingException;
import de.vsy.chat.module.packet_processing.ContentProcessor;

/**
 * PacketProcessor for reconnection requests.
 *
 * <p>Frederic Heath
 */
public class ReconnectResponseProcessor implements ContentProcessor<ReconnectResponseDTO> {

  private final AuthenticationDataModelAccess dataModel;

  /**
   * Instantiates a new reconnect request handler.
   *
   * @param dataModel the update unit
   */
  public ReconnectResponseProcessor(final AuthenticationDataModelAccess dataModel) {
    this.dataModel = dataModel;
  }

  @Override
  public PacketContent processContent(ReconnectResponseDTO toProcess)
      throws PacketProcessingException {
    this.dataModel.completeReconnect(toProcess.getReconnectionState());
    return null;
  }
}
