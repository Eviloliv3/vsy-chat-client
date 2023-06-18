/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.vsy.client.packet_processing.content_processing;

import de.vsy.client.packet_processing.ResultingContentHandlingProvider;
import de.vsy.client.packet_processing.ResultingPacketContentHandler;
import de.vsy.shared_module.packet_processing.ContentProcessor;
import de.vsy.shared_transmission.packet.content.authentication.AccountCreationRequestDTO;

/**
 * PacketProcessor for account creation requests.
 *
 * <p>Frederic Heath
 */
public class NewAccountRequestProcessor implements ContentProcessor<AccountCreationRequestDTO> {

  private final ResultingPacketContentHandler contentHandler;

  /**
   * Instantiates a new account request handler.
   */
  public NewAccountRequestProcessor(final ResultingContentHandlingProvider handlerProvider) {
    this.contentHandler = handlerProvider.getResultingPacketContentHandler();
  }

  @Override
  public void processContent(AccountCreationRequestDTO toProcess) {
    this.contentHandler.addRequest(toProcess);
  }
}
