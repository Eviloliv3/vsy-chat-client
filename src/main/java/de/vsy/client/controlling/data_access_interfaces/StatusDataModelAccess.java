/*
 *
 */
package de.vsy.client.controlling.data_access_interfaces;

import de.vsy.shared_transmission.dto.CommunicatorDTO;
import de.vsy.shared_transmission.packet.content.HumanInteractionRequest;
import de.vsy.shared_transmission.packet.content.chat.TextMessageDTO;
import de.vsy.shared_transmission.packet.content.relation.EligibleContactEntity;
import java.util.List;
import java.util.Map;

/**
 * Provides methods for simple dataManagement manipulation from setup packet handlers.
 */
public interface StatusDataModelAccess extends EssentialDataModelAccess {

  /**
   * Setup messenger.
   *
   * @param messages       the messages
   * @param activeContacts the active clients
   */
  void setupMessenger(
      Map<Integer, List<TextMessageDTO>> messages,
      Map<EligibleContactEntity, List<CommunicatorDTO>> activeContacts);

  /**
   * Tear down messenger.
   */
  void tearDownMessenger();

  /**
   * Adds the contact dataManagement.
   *
   * @param contactData the contact dataManagement
   * @param oldMessages the old messages
   */
  void addContactData(
      final EligibleContactEntity contactType,
      CommunicatorDTO contactData,
      List<TextMessageDTO> oldMessages);

  /**
   * Adds the request.
   *
   * @param request the request
   */
  void addRequest(HumanInteractionRequest request);

  /**
   * Removes the contact dataManagement.
   *
   * @param contactType the contact type
   * @param contactData the contact dataManagement
   */
  void removeContactData(
      final EligibleContactEntity contactType, final CommunicatorDTO contactData);
}
