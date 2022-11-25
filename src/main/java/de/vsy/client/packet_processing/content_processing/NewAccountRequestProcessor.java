/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.vsy.client.packet_processing.content_processing;

import de.vsy.client.controlling.data_access_interfaces.AuthenticationDataModelAccess;
import de.vsy.shared_module.packet_processing.ContentProcessor;
import de.vsy.shared_transmission.packet.content.PacketContent;
import de.vsy.shared_transmission.packet.content.authentication.NewAccountRequestDTO;

/**
 * PacketProcessor for account creation requests.
 *
 * <p>Frederic Heath
 */
public class NewAccountRequestProcessor implements ContentProcessor<NewAccountRequestDTO> {

  private final AuthenticationDataModelAccess dataModel;

  /**
   * Instantiates a new account request handler.
   *
   * @param dataModel the update unit
   */
  public NewAccountRequestProcessor(final AuthenticationDataModelAccess dataModel) {
    this.dataModel = dataModel;
  }

  @Override
  public PacketContent processContent(NewAccountRequestDTO toProcess) {
    return toProcess;
  }
}
