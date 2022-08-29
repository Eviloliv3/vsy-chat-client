/*
 *
 */
package de.vsy.chat.client.packet_processing.content_processing;

import de.vsy.chat.transmission.packet.content.PacketContent;
import de.vsy.chat.transmission.packet.content.error.ErrorDTO;
import de.vsy.chat.client.controlling.data_access_interfaces.EssentialDataModelAccess;
import de.vsy.chat.module.packet_exception.PacketProcessingException;
import de.vsy.chat.module.packet_processing.ContentProcessor;

/**
 * PacketProcessor for error type Packet.
 *
 * <p>Frederic Heath
 */
public class SimpleErrorProcessor implements ContentProcessor<ErrorDTO> {

  private final EssentialDataModelAccess dataModel;

  /**
   * Instantiates a new simple error handler.
   *
   * @param dataModel the update unit
   */
  public SimpleErrorProcessor(final EssentialDataModelAccess dataModel) {
    this.dataModel = dataModel;
  }

  @Override
  public PacketContent processContent(ErrorDTO toProcess) throws PacketProcessingException {
    this.dataModel.addError(toProcess);
    return toProcess;
  }
}
