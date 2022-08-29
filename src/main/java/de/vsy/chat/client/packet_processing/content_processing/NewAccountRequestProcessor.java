/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.vsy.chat.client.packet_processing.content_processing;

import de.vsy.chat.transmission.packet.content.PacketContent;
import de.vsy.chat.transmission.packet.content.authentication.NewAccountRequestDTO;
import de.vsy.chat.client.controlling.data_access_interfaces.AuthenticationDataModelAccess;
import de.vsy.chat.module.packet_exception.PacketProcessingException;
import de.vsy.chat.module.packet_processing.ContentProcessor;

/**
 * PacketProcessor for account creation requests.
 *
 * <p>Frederic Heath
 */
public class NewAccountRequestProcessor implements ContentProcessor<NewAccountRequestDTO> {

  private final AuthenticationDataModelAccess dataModel;

  /**
   * Instantiates a new new account request handler.
   *
   * @param dataModel the update unit
   */
  public NewAccountRequestProcessor(final AuthenticationDataModelAccess dataModel) {
    this.dataModel = dataModel;
  }

  @Override
  public PacketContent processContent(NewAccountRequestDTO toProcess)
      throws PacketProcessingException {
    return toProcess;
  }
}
