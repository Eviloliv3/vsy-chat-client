package de.vsy.client.packet_processing.content_processing;

import de.vsy.client.controlling.data_access_interfaces.StatusDataModelAccess;
import de.vsy.shared_module.packet_processing.ContentProcessor;
import de.vsy.shared_transmission.packet.content.PacketContent;
import de.vsy.shared_transmission.packet.content.status.ClientStatusChangeDTO;

public class ClientStatusProcessor implements ContentProcessor<ClientStatusChangeDTO> {

  private final StatusDataModelAccess dataModel;

  public ClientStatusProcessor(StatusDataModelAccess dataModel) {
    this.dataModel = dataModel;
  }

  @Override
  public PacketContent processContent(ClientStatusChangeDTO toProcess) {
    return toProcess;
  }
}
