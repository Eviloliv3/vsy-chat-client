package de.vsy.chat.client.packet_processing.content_processing;

import de.vsy.chat.client.controlling.data_access_interfaces.AuthenticationDataModelAccess;
import de.vsy.chat.module.packet_processing.ContentProcessor;
import de.vsy.chat.transmission.packet.content.PacketContent;
import de.vsy.chat.transmission.packet.content.authentication.LoginRequestDTO;

public class LoginRequestProcessor implements ContentProcessor<LoginRequestDTO> {

  private final AuthenticationDataModelAccess dataModel;

  /**
   * Instantiates a new login response handler.
   *
   * @param dataModel the dataManagement model
   */
  public LoginRequestProcessor(final AuthenticationDataModelAccess dataModel) {
    this.dataModel = dataModel;
  }

  @Override
  public PacketContent processContent(LoginRequestDTO toProcess) {
    return toProcess;
  }
}
