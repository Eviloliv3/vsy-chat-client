package de.vsy.client.packet_processing.content_processing;

import de.vsy.client.controlling.data_access_interfaces.AuthenticationDataModelAccess;
import de.vsy.client.data_model.notification.SimpleInformation;
import de.vsy.shared_module.packet_processing.ContentProcessor;
import de.vsy.shared_transmission.packet.content.authentication.ReconnectResponseDTO;

/**
 * PacketProcessor for reconnection requests.
 *
 * <p>Frederic Heath
 */
public class ReconnectResponseProcessor implements ContentProcessor<ReconnectResponseDTO> {

  private final AuthenticationDataModelAccess dataModel;

  /**
   * Instantiates a new reconnect request handler.
   *
   * @param dataModel the update unit
   */
  public ReconnectResponseProcessor(final AuthenticationDataModelAccess dataModel) {
    this.dataModel = dataModel;
  }

  @Override
  public void processContent(ReconnectResponseDTO toProcess) {
    if (toProcess.getReconnectionState()) {
      this.dataModel.completeReconnect(toProcess.getReconnectionState());
    } else {
      this.dataModel.addNotification(
          new SimpleInformation("Authentication failed. Please reenter your credentials."));
      this.dataModel.completeLogout();
    }
  }
}
