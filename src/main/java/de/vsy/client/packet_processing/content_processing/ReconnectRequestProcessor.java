package de.vsy.client.packet_processing.content_processing;

import de.vsy.client.controlling.data_access_interfaces.AuthenticationDataModelAccess;
import de.vsy.shared_module.packet_processing.ContentProcessor;
import de.vsy.shared_transmission.packet.content.PacketContent;
import de.vsy.shared_transmission.packet.content.authentication.ReconnectRequestDTO;

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
  public PacketContent processContent(ReconnectRequestDTO toProcess) {
    return toProcess;
  }
}
