package de.vsy.chat.client.packet_processing.content_processing;

import de.vsy.chat.transmission.packet.content.PacketContent;
import de.vsy.chat.transmission.packet.content.status.MessengerTearDownDTO;
import de.vsy.chat.client.controlling.data_access_interfaces.StatusDataModelAccess;
import de.vsy.chat.module.packet_exception.PacketProcessingException;
import de.vsy.chat.module.packet_processing.ContentProcessor;

public class MessengerTearDownProcessor implements ContentProcessor<MessengerTearDownDTO> {

  private final StatusDataModelAccess dataModel;

  /**
   * Instantiates a new messenger setup handler.
   *
   * @param dataModel the update unit
   */
  public MessengerTearDownProcessor(final StatusDataModelAccess dataModel) {
    this.dataModel = dataModel;
  }

  @Override
  public PacketContent processContent(MessengerTearDownDTO toProcess)
      throws PacketProcessingException {
    if (toProcess.getNewMessengerState()) {
      this.dataModel.tearDownMessenger();
    } else {
      this.dataModel.addInformation("Abmeldung vom Chat-Dienst ist fehlgeschlagen.");
    }
    return null;
  }
}
