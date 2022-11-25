package de.vsy.client.packet_processing.content_processing;

import de.vsy.client.controlling.data_access_interfaces.StatusDataModelAccess;
import de.vsy.shared_module.packet_processing.ContentProcessor;
import de.vsy.shared_transmission.packet.content.PacketContent;
import de.vsy.shared_transmission.packet.content.status.MessengerSetupDTO;

public class MessengerSetupProcessor implements ContentProcessor<MessengerSetupDTO> {

  private final StatusDataModelAccess dataModel;

  /**
   * Instantiates a new messenger setup handler.
   *
   * @param dataModel the update unit
   */
  public MessengerSetupProcessor(final StatusDataModelAccess dataModel) {
    this.dataModel = dataModel;
  }

  @Override
  public PacketContent processContent(MessengerSetupDTO toProcess) {
    this.dataModel.setupMessenger(toProcess.getOldMessages(), toProcess.getActiveContacts());
    return null;
  }
}
