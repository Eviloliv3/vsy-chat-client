package de.vsy.chat.client.packet_processing.content_processing;

import de.vsy.chat.transmission.packet.content.PacketContent;
import de.vsy.chat.transmission.packet.content.status.ClientStatusChangeDTO;
import de.vsy.chat.client.controlling.data_access_interfaces.StatusDataModelAccess;
import de.vsy.chat.module.packet_exception.PacketProcessingException;
import de.vsy.chat.module.packet_processing.ContentProcessor;

public class ClientStatusProcessor implements ContentProcessor<ClientStatusChangeDTO> {

  private final StatusDataModelAccess dataModel;

  public ClientStatusProcessor(StatusDataModelAccess dataModel) {
    this.dataModel = dataModel;
  }

  @Override
  public PacketContent processContent(ClientStatusChangeDTO toProcess)
      throws PacketProcessingException {
    return toProcess;
  }
}
