package de.vsy.client.packet_processing.content_processing;

import de.vsy.client.packet_processing.ResultingContentHandlingProvider;
import de.vsy.client.packet_processing.ResultingPacketContentHandler;
import de.vsy.shared_module.packet_exception.PacketProcessingException;
import de.vsy.shared_module.packet_processing.ContentProcessor;
import de.vsy.shared_transmission.packet.content.authentication.AccountDeletionRequestDTO;

public class AccountDeletionRequestProcessor implements
    ContentProcessor<AccountDeletionRequestDTO> {

  private final ResultingPacketContentHandler contentHandler;

  public AccountDeletionRequestProcessor(final ResultingContentHandlingProvider handlerProvider) {
    this.contentHandler = handlerProvider.getResultingPacketContentHandler();
  }

  @Override
  public void processContent(AccountDeletionRequestDTO toProcess) throws PacketProcessingException {
    contentHandler.addRequest(toProcess);
  }
}
