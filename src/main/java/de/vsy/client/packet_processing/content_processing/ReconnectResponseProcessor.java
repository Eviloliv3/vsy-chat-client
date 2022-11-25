/*
 *
 */
package de.vsy.client.packet_processing.content_processing;

import de.vsy.client.controlling.data_access_interfaces.AuthenticationDataModelAccess;
import de.vsy.shared_module.packet_processing.ContentProcessor;
import de.vsy.shared_transmission.packet.content.PacketContent;
import de.vsy.shared_transmission.packet.content.authentication.ReconnectResponseDTO;

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
  public PacketContent processContent(ReconnectResponseDTO toProcess) {
    this.dataModel.completeReconnect(toProcess.getReconnectionState());
    return null;
  }
}
