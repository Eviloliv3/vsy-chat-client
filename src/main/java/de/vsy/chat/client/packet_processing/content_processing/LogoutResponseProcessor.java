/*
 *
 */
package de.vsy.chat.client.packet_processing.content_processing;

import de.vsy.chat.transmission.packet.content.PacketContent;
import de.vsy.chat.transmission.packet.content.authentication.LogoutResponseDTO;
import de.vsy.chat.client.controlling.data_access_interfaces.AuthenticationDataModelAccess;
import de.vsy.chat.module.packet_exception.PacketProcessingException;
import de.vsy.chat.module.packet_processing.ContentProcessor;

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
  public PacketContent processContent(LogoutResponseDTO toProcess)
      throws PacketProcessingException {
    if (toProcess.getLogoutState()) {
      this.dataModel.completeLogout();
    }
    return null;
  }
}
