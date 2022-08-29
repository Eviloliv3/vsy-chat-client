package de.vsy.chat.client.packet_processing.content_processing;

import de.vsy.chat.transmission.packet.content.PacketContent;
import de.vsy.chat.transmission.packet.content.authentication.LogoutRequestDTO;
import de.vsy.chat.client.controlling.data_access_interfaces.AuthenticationDataModelAccess;
import de.vsy.chat.module.packet_exception.PacketProcessingException;
import de.vsy.chat.module.packet_processing.ContentProcessor;

public class LogoutRequestProcessor implements ContentProcessor<LogoutRequestDTO> {

  private final AuthenticationDataModelAccess dataModel;

  public LogoutRequestProcessor(final AuthenticationDataModelAccess dataModel) {
    this.dataModel = dataModel;
  }

  @Override
  public PacketContent processContent(LogoutRequestDTO toProcess) throws PacketProcessingException {
    return toProcess;
  }
}
