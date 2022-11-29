package de.vsy.client.packet_processing.content_processing;

import de.vsy.client.controlling.data_access_interfaces.AuthenticationDataModelAccess;
import de.vsy.client.packet_processing.ResultingContentHandlingProvider;
import de.vsy.client.packet_processing.ResultingPacketContentHandler;
import de.vsy.shared_module.packet_processing.ContentProcessor;
import de.vsy.shared_transmission.packet.content.authentication.ReconnectRequestDTO;

public class ReconnectRequestProcessor implements ContentProcessor<ReconnectRequestDTO> {

  private final ResultingPacketContentHandler contentHandler;

  /**
   * Instantiates a new reconnect request handler.
   *
   * @param dataModel the update unit
   */
  public ReconnectRequestProcessor(final AuthenticationDataModelAccess dataModel,
      final ResultingContentHandlingProvider handlerProvider) {
    this.contentHandler = handlerProvider.getResultingPacketContentHandler();
  }

  @Override
  public void processContent(ReconnectRequestDTO toProcess) {
    this.contentHandler.addRequest(toProcess);
  }
}
