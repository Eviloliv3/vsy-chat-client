package de.vsy.client.packet_processing.content_processing;

import de.vsy.client.controlling.data_access_interfaces.AuthenticationDataModelAccess;
import de.vsy.shared_module.packet_processing.ContentProcessor;
import de.vsy.shared_transmission.packet.content.authentication.LoginResponseDTO;

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
  public void processContent(LoginResponseDTO toProcess) {
    this.dataModel.addClientData(toProcess.getClientData());
  }
}
