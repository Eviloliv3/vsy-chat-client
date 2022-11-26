package de.vsy.client.packet_processing.content_processing;

import de.vsy.client.controlling.data_access_interfaces.AuthenticationDataModelAccess;
import de.vsy.client.packet_processing.ResultingContentHandlingProvider;
import de.vsy.client.packet_processing.ResultingPacketContentHandler;
import de.vsy.shared_module.packet_processing.ContentProcessor;
import de.vsy.shared_transmission.packet.content.authentication.LogoutRequestDTO;

public class LogoutRequestProcessor implements ContentProcessor<LogoutRequestDTO> {

  private final AuthenticationDataModelAccess dataModel;
  private final ResultingPacketContentHandler contentHandler;

  public LogoutRequestProcessor(final AuthenticationDataModelAccess dataModel,
      final ResultingContentHandlingProvider handlerProvider) {
    this.dataModel = dataModel;
    this.contentHandler = handlerProvider.getResultingPacketContentHandler();
  }

  @Override
  public void processContent(LogoutRequestDTO toProcess) {
    this.contentHandler.addRequest(toProcess);
  }
}
