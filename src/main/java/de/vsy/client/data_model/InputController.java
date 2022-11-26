/*
 *
 */
package de.vsy.client.data_model;

import static de.vsy.shared_utility.standard_value.StandardIdProvider.STANDARD_CLIENT_ID;

import de.vsy.client.controlling.StatusMessageTriggeredActions;
import de.vsy.client.controlling.data_access_interfaces.AuthenticationDataModelAccess;
import de.vsy.client.controlling.data_access_interfaces.ChatDataModelAccess;
import de.vsy.client.controlling.data_access_interfaces.StatusDataModelAccess;
import de.vsy.shared_module.data_element_validation.IdCheck;
import de.vsy.shared_transmission.dto.CommunicatorDTO;
import de.vsy.shared_transmission.packet.content.HumanInteractionRequest;
import de.vsy.shared_transmission.packet.content.Translatable;
import de.vsy.shared_transmission.packet.content.chat.TextMessageDTO;
import de.vsy.shared_transmission.packet.content.relation.EligibleContactEntity;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class InputController
    implements AuthenticationDataModelAccess, ChatDataModelAccess, StatusDataModelAccess {

  private final StatusMessageTriggeredActions dataTrigger;
  private final ServerDataCache serverDataModel;

  /**
   * Instantiates a new dataManagement input controller.
   *
   * @param dataModel   the dataManagement model
   * @param dataTrigger the dataManagement trigger
   */
  public InputController(final ServerDataCache dataModel,
      final StatusMessageTriggeredActions dataTrigger) {
    this.serverDataModel = dataModel;
    this.dataTrigger = dataTrigger;
  }

  @Override
  public void addClientData(final CommunicatorDTO clientData) {

    if (clientData != null) {
      this.serverDataModel.setCommunicatorDTO(clientData);
      this.dataTrigger.completeLogin();
    } else if (this.serverDataModel.getCommunicatorId() != STANDARD_CLIENT_ID) {
      this.dataTrigger.reconnectFailed();
    }
  }

  @Override
  public void completeLogout() {
    this.serverDataModel.resetAllData();
    this.dataTrigger.completeLogout();
  }

  @Override
  public void completeReconnect(final boolean reconnectionSuccess) {

    if (!reconnectionSuccess) {
      this.dataTrigger.reconnectFailed();
    }
  }

  @Override
  public void addNotification(final Translatable notification) {
    this.serverDataModel.addNotification(notification);
  }

  @Override
  public int getClientId() {
    return this.serverDataModel.getCommunicatorId();
  }

  @Override
  public boolean isClientLoggedIn() {
    final var clientId = getClientId();
    return IdCheck.checkData(clientId).isEmpty() && clientId > 0;
  }

  @Override
  public void addMessage(final TextMessageDTO message) {
    this.serverDataModel.addMessage(message);
    this.dataTrigger.messageReceived(message);
  }

  /**
   * Setup messenger.
   *
   * @param messages       the messages
   * @param activeContacts the active clients
   */
  @Override
  public void setupMessenger(
      final Map<Integer, List<TextMessageDTO>> messages,
      final Map<EligibleContactEntity, Set<CommunicatorDTO>> activeContacts) {
    this.serverDataModel.initialMessengerSetup(messages, activeContacts);
    this.dataTrigger.completeMessengerSetup();
  }

  @Override
  public void tearDownMessenger() {
    this.serverDataModel.resetAllData();
    this.dataTrigger.completeMessengerTearDown();
  }

  /**
   * Adds the contact dataManagement.
   *
   * @param contactData the contact dataManagement
   * @param oldMessages the old messages
   */
  @Override
  public void addContactData(
      final EligibleContactEntity contactType,
      final CommunicatorDTO contactData,
      final List<TextMessageDTO> oldMessages) {
    this.serverDataModel.addContact(contactType, contactData, oldMessages);
    this.dataTrigger.changeContactStatus(contactData, true);
  }

  @Override
  public void addRequest(final HumanInteractionRequest request) {
    this.serverDataModel.addNotification(request);
  }

  @Override
  public void removeContactData(
      final EligibleContactEntity contactType, final CommunicatorDTO contactData) {
    this.serverDataModel.removeContact(contactType, contactData);
    this.dataTrigger.changeContactStatus(contactData, false);
  }
}
