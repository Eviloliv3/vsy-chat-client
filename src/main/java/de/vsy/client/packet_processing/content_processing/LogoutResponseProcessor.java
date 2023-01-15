
package de.vsy.client.packet_processing.content_processing;

import de.vsy.client.controlling.data_access_interfaces.AuthenticationDataModelAccess;
import de.vsy.shared_module.packet_processing.ContentProcessor;
import de.vsy.shared_transmission.packet.content.authentication.LogoutResponseDTO;

public class LogoutResponseProcessor implements ContentProcessor<LogoutResponseDTO> {

  private final AuthenticationDataModelAccess dataModel;

  /**
   * Instantiates a new logout output handler.
   *
   * @param dataModel the dataManagement model
   */
  public LogoutResponseProcessor(final AuthenticationDataModelAccess dataModel) {
    this.dataModel = dataModel;
  }

  @Override
  public void processContent(LogoutResponseDTO toProcess) {
    if (toProcess.getLogoutState()) {
      this.dataModel.completeLogout();
    }
  }
}
