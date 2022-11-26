package de.vsy.client.packet_processing.content_processing;

import de.vsy.client.controlling.data_access_interfaces.AuthenticationDataModelAccess;
import de.vsy.client.packet_processing.ResultingContentHandlingProvider;
import de.vsy.client.packet_processing.ResultingPacketContentHandler;
import de.vsy.shared_module.packet_processing.ContentProcessor;
import de.vsy.shared_transmission.packet.content.authentication.LoginRequestDTO;

public class LoginRequestProcessor implements ContentProcessor<LoginRequestDTO> {

  private final AuthenticationDataModelAccess dataModel;
  private final ResultingPacketContentHandler contentHandler;

  /**
   * Instantiates a new login response handler.
   *
   * @param dataModel the dataManagement model
   */
  public LoginRequestProcessor(final AuthenticationDataModelAccess dataModel,
      final ResultingContentHandlingProvider handlerProvider) {
    this.dataModel = dataModel;
    this.contentHandler = handlerProvider.getResultingPacketContentHandler();
  }

  @Override
  public void processContent(LoginRequestDTO toProcess) {
    contentHandler.addRequest(toProcess);
  }
}
