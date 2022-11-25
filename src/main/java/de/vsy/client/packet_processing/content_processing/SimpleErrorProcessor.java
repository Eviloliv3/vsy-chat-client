/*
 *
 */
package de.vsy.client.packet_processing.content_processing;

import de.vsy.client.controlling.data_access_interfaces.EssentialDataModelAccess;
import de.vsy.shared_module.packet_processing.ContentProcessor;
import de.vsy.shared_transmission.packet.content.PacketContent;
import de.vsy.shared_transmission.packet.content.error.ErrorDTO;

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
  public PacketContent processContent(ErrorDTO toProcess) {
    this.dataModel.addNotification(toProcess);
    return toProcess;
  }
}
