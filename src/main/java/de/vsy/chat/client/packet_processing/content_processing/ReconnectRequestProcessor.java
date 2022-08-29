package de.vsy.chat.client.packet_processing.content_processing;

import de.vsy.chat.transmission.packet.content.PacketContent;
import de.vsy.chat.transmission.packet.content.authentication.ReconnectRequestDTO;
import de.vsy.chat.client.controlling.data_access_interfaces.AuthenticationDataModelAccess;
import de.vsy.chat.module.packet_exception.PacketProcessingException;
import de.vsy.chat.module.packet_processing.ContentProcessor;

public class ReconnectRequestProcessor implements ContentProcessor<ReconnectRequestDTO> {

  private final AuthenticationDataModelAccess dataModel;

  /**
   * Instantiates a new reconnect request handler.
   *
   * @param dataModel the update unit
   */
  public ReconnectRequestProcessor(final AuthenticationDataModelAccess dataModel) {
    this.dataModel = dataModel;
  }

  @Override
  public PacketContent processContent(ReconnectRequestDTO toProcess)
      throws PacketProcessingException {
    return toProcess;
  }
}
