package de.vsy.client.packet_processing.content_processing;

import de.vsy.client.controlling.data_access_interfaces.AuthenticationDataModelAccess;
import de.vsy.shared_module.packet_processing.ContentProcessor;
import de.vsy.shared_transmission.packet.content.PacketContent;
import de.vsy.shared_transmission.packet.content.authentication.LogoutRequestDTO;

public class LogoutRequestProcessor implements ContentProcessor<LogoutRequestDTO> {

  private final AuthenticationDataModelAccess dataModel;

  public LogoutRequestProcessor(final AuthenticationDataModelAccess dataModel) {
    this.dataModel = dataModel;
  }

  @Override
  public PacketContent processContent(LogoutRequestDTO toProcess) {
    return toProcess;
  }
}
