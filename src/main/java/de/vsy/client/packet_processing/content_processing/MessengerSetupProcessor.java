package de.vsy.client.packet_processing.content_processing;

import de.vsy.client.controlling.data_access_interfaces.StatusDataModelAccess;
import de.vsy.shared_module.packet_processing.ContentProcessor;
import de.vsy.shared_transmission.dto.CommunicatorDTO;
import de.vsy.shared_transmission.packet.content.relation.EligibleContactEntity;
import de.vsy.shared_transmission.packet.content.status.MessengerSetupDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
  public void processContent(MessengerSetupDTO toProcess) {
    Map<EligibleContactEntity, List<CommunicatorDTO>> contacts = new HashMap<>();

    for (final var contactMapping : toProcess.getActiveContacts().entrySet()) {
      final var listedContacts = new ArrayList<CommunicatorDTO>();
      Set<CommunicatorDTO> receivedContacts = contactMapping.getValue();

      if (receivedContacts != null) {
        listedContacts.addAll(receivedContacts);
      }
      contacts.put(contactMapping.getKey(), listedContacts);
    }
    this.dataModel.setupMessenger(toProcess.getOldMessages(), contacts);
  }
}
