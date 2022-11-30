package de.vsy.client.packet_processing.content_processing;

import de.vsy.client.controlling.data_access_interfaces.StatusDataModelAccess;
import de.vsy.client.data_model.notification.SimpleInformation;
import de.vsy.shared_module.packet_processing.ContentProcessor;
import de.vsy.shared_transmission.packet.content.status.MessengerTearDownDTO;

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
  public void processContent(MessengerTearDownDTO toProcess) {
    if (toProcess.getNewMessengerState()) {
      this.dataModel.tearDownMessenger();
    } else {
      this.dataModel.addNotification(new SimpleInformation(
          "Checkout from messenger service failed."));
    }
  }
}
