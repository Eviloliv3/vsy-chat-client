package de.vsy.client.packet_processing.content_processing;

import de.vsy.client.controlling.data_access_interfaces.StatusDataModelAccess;
import de.vsy.client.packet_processing.ResultingContentHandlingProvider;
import de.vsy.client.packet_processing.ResultingPacketContentHandler;
import de.vsy.shared_module.packet_processing.ContentProcessor;
import de.vsy.shared_transmission.packet.content.status.ClientStatusChangeDTO;

public class ClientStatusProcessor implements ContentProcessor<ClientStatusChangeDTO> {

  private final ResultingPacketContentHandler contentHandler;


  public ClientStatusProcessor(StatusDataModelAccess dataModel,
      ResultingContentHandlingProvider handlerProvider) {
    this.contentHandler = handlerProvider.getResultingPacketContentHandler();
  }

  @Override
  public void processContent(ClientStatusChangeDTO validatedContent) {
    contentHandler.addRequest(validatedContent);
  }
}
