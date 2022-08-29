package de.vsy.chat.client.packet_processing.content_processing;

import de.vsy.chat.transmission.packet.content.PacketContent;
import de.vsy.chat.transmission.packet.content.authentication.LoginResponseDTO;
import de.vsy.chat.client.controlling.data_access_interfaces.AuthenticationDataModelAccess;
import de.vsy.chat.module.packet_exception.PacketProcessingException;
import de.vsy.chat.module.packet_processing.ContentProcessor;

public class LoginResponseProcessor implements ContentProcessor<LoginResponseDTO> {

  private final AuthenticationDataModelAccess dataModel;

  /**
   * Instantiates a new login response handler.
   *
   * @param dataModel the dataManagement model
   */
  public LoginResponseProcessor(final AuthenticationDataModelAccess dataModel) {
    this.dataModel = dataModel;
  }

  @Override
  public PacketContent processContent(LoginResponseDTO toProcess) throws PacketProcessingException {
    this.dataModel.addClientData(toProcess.getClientData());
    return null;
  }
}
