/*
 *
 */
package de.vsy.chat.client.data_model;

import de.vsy.chat.client.controlling.StatusMessageTriggeredActions;
import de.vsy.chat.client.controlling.data_access_interfaces.AuthenticationDataModelAccess;
import de.vsy.chat.client.controlling.data_access_interfaces.ChatDataModelAccess;
import de.vsy.chat.client.controlling.data_access_interfaces.StatusDataModelAccess;
import de.vsy.chat.client.packet_processing.PacketProcessingService;
import de.vsy.chat.module.data_element_validation.IdCheck;
import de.vsy.chat.transmission.dto.CommunicatorDTO;
import de.vsy.chat.transmission.packet.content.HumanInteractionRequest;
import de.vsy.chat.transmission.packet.content.chat.TextMessageDTO;
import de.vsy.chat.transmission.packet.content.error.ErrorDTO;
import de.vsy.chat.transmission.packet.content.relation.EligibleContactEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import static de.vsy.chat.utility.standard_value.StandardIdProvider.STANDARD_CLIENT_ID;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import org.apache.logging.log4j.LogManager;

/** Fassade fuer Daten vom Server. */
public class DataInputController
    implements AuthenticationDataModelAccess, ChatDataModelAccess, StatusDataModelAccess {

  private final StatusMessageTriggeredActions dataTrigger;
  private final ExecutorService packetProcessingService;
  private final ServerProvidedDataManager serverDataModel;
  private PacketProcessingService packetProcessingLogic;

  /**
   * Instantiates a new dataManagement input controller.
   *
   * @param dataModel the dataManagement model
   * @param PacketBuffers the Packetbuffers
   * @param dataTrigger the dataManagement trigger
   */
  public DataInputController(
      final ServerProvidedDataManager dataModel, final StatusMessageTriggeredActions dataTrigger) {
    this.packetProcessingService = newFixedThreadPool(1);

    this.serverDataModel = dataModel;
    this.dataTrigger = dataTrigger;
  }

  // -------------------------------------------
  // ---Status-Data-Model-Access----------------
  @Override
  public void addClientData(final CommunicatorDTO clientData) {

    if (clientData != null) {
      this.serverDataModel.setCommunicatorDTO(clientData);
      this.dataTrigger.loginComplete();
    } else if (this.serverDataModel.getCommunicatorId() != STANDARD_CLIENT_ID) {
      this.dataTrigger.reconnectFailed();
    }
  }

  @Override
  public void completeLogout() {
    this.serverDataModel.resetAllData();
    this.dataTrigger.logoutComplete();
  }

  @Override
  public void completeReconnect(final boolean reconnectionSuccess) {

    if (!reconnectionSuccess) {
      this.dataTrigger.reconnectFailed();
    }
  }

  @Override
  public void addError(final ErrorDTO error) {
    this.serverDataModel.addDialogMessage(error);
  }

  @Override
  public void addInformation(final String info) {
    this.serverDataModel.addDialogMessage(info);
  }

  @Override
  public int getClientId() {
    return this.serverDataModel.getCommunicatorId();
  }

  @Override
  public boolean isClientLoggedIn() {
    final var clientId = getClientId();
    return IdCheck.checkData(clientId) == null && clientId > 0;
  }

  @Override
  public void addMessage(final TextMessageDTO message) {
    this.serverDataModel.addMessage(message);
    this.dataTrigger.messageReceived(message);
  }

  /**
   * Close controller.
   *
   * @throws InterruptedException the interrupted exception
   */
  public void closeController() throws InterruptedException {
    this.packetProcessingService.shutdownNow();
    do {
      LogManager.getLogger().info("Es wird noch auf den " + "Paketverarbeitungsservice gewartet.");
      Thread.yield();
    } while (this.packetProcessingService.awaitTermination(1000, MILLISECONDS));
  }

  /**
   * Setup messenger.
   *
   * @param messages the messages
   * @param activeContacts the active clients
   */
  // -------------------------------------------
  @Override
  public void setupMessenger(
      final Map<Integer, List<TextMessageDTO>> messages,
      final Map<EligibleContactEntity, Set<CommunicatorDTO>> activeContacts) {
    this.serverDataModel.initialMessengerSetup(messages, activeContacts);
    this.dataTrigger.messengerSetupComplete();
  }

  @Override
  public void tearDownMessenger() {
    this.serverDataModel.resetAllData();
    this.dataTrigger.messengerTearDownComplete();
  }

  /**
   * Adds the contact dataManagement.
   *
   * @param contactData the contact dataManagement
   * @param oldMessages the old messages
   */
  // -------------------------------------------
  @Override
  public void addContactData(
      final EligibleContactEntity contactType,
      final CommunicatorDTO contactData,
      final List<TextMessageDTO> oldMessages) {
    this.serverDataModel.addContact(contactType, contactData, oldMessages);
    this.dataTrigger.contactOnlineStatusChange(contactData, true);
  }

  @Override
  public void addRequest(final HumanInteractionRequest request) {
    this.serverDataModel.addRequest(request);
  }

  @Override
  public void removeContactData(
      final EligibleContactEntity contactType, final CommunicatorDTO contactData) {
    this.serverDataModel.removeContact(contactType, contactData);
    this.dataTrigger.contactOnlineStatusChange(contactData, false);
  }

  public void startPacketProcessing() {
    this.packetProcessingService.execute(this.packetProcessingLogic);
  }
}
