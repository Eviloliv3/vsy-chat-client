/*
 *
 */
package de.vsy.chat.client.gui;

import de.vsy.chat.client.controlling.DialogRequestProcessor;
import de.vsy.chat.client.controlling.packet_creation.RequestPacketCreator;
import de.vsy.chat.client.data_model.GUIStateDataManager;
import de.vsy.chat.client.data_model.ServerProvidedDataManager;
import de.vsy.chat.client.gui.chatter_main_model.ClientChatGUI;
import de.vsy.chat.client.gui.chatter_main_model.GUILogic;
import de.vsy.chat.client.gui.essential_graphical_units.ChatterMenu;
import de.vsy.chat.client.gui.essential_graphical_units.dialog.DialogType;

import static de.vsy.chat.client.gui.essential_graphical_units.dialog.DialogStatus.INITIAL;
import static de.vsy.chat.utility.standard_value.StandardIdProvider.STANDARD_CLIENT_ID;

import de.vsy.chat.transmission.dto.CommunicatorDTO;

import java.util.concurrent.ExecutorService;
import static java.util.concurrent.Executors.newFixedThreadPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Basic Controller for initiating and destroying the GUI and the appertaining logic. */
public class GUIController implements DialogRequestProcessor {

  private final Logger logger;
  private final ClientChatGUI gui;
  private final GUIStateDataManager guiDataModel;
  private final GUILogic guiLogic;
  private final ServerProvidedDataManager serverDataModel;
  private final ExecutorService threadPool;

  /**
   * Instantiates a new GUI controller.
   *
   * @param gui the gui
   * @param serverData the server dataManagement
   * @param guiData the gui dataManagement
   * @param requester the requester
   */
  public GUIController(
      final ClientChatGUI gui,
      final ServerProvidedDataManager serverData,
      final GUIStateDataManager guiData,
      final RequestPacketCreator requester) {
    this.logger = LogManager.getLogger();
    this.threadPool = newFixedThreadPool(1);
    this.gui = gui;
    this.serverDataModel = serverData;
    this.guiDataModel = guiData;
    this.guiLogic = new GUILogic(this.gui, this.serverDataModel, this.guiDataModel, requester);
  }

  public void closeController() {
    this.threadPool.shutdownNow();

    // TODO hier sollten alle Dialoge entfernt werden
    try {
      Thread.sleep(1000);
    } catch (final InterruptedException e) {
      Thread.currentThread().interrupt();
      this.logger.error("Beim Warten auf das Schliessen aller Dialoge unterbrochen.");
    }
  }

  /**
   * Contact status change.
   *
   * @param contactData the contact dataManagement
   * @param onlineStatus the online status
   */
  public void contactStatusChange(final CommunicatorDTO contactData, final boolean onlineStatus) {

    if (onlineStatus) {
      addContactsToGui();
    } else {
      this.guiDataModel.removeContact(contactData);
    }
  }

  public void addContactsToGui() {
    var contacts = this.serverDataModel.getContactList();

    this.guiDataModel.getContactListModel().removeAllElements();

    for (var currentContact : contacts) {
      this.guiDataModel.addContact(currentContact);
    }
  }

  /**
   * Gui termination state.
   *
   * @return true, if successful
   */
  public boolean guiTerminationState() {
    return this.guiLogic.getCloseFlag();
  }

  public void initGUIControlling() {
    this.gui.initGUIControl(this.guiLogic, new ChatterMenu(this.guiLogic));
    this.gui.setMessageHistory(this.guiDataModel.getActiveChatHistory());
    this.gui.setContactListModel(this.guiDataModel.getContactListModel());
  }

  // -------------------------------------------
  // ---Dialog-Request-Handler------------------
  /**
   * New dialog request.
   *
   * @param type the type
   */
  // -------------------------------------------
  @Override
  public void newDialogRequest(final DialogType type) {
    this.guiLogic.handleDialogRequest(type);
  }

  public void processClientData() {
    final var communicationEntity =
        this.serverDataModel.getClientAccountData().getCommunicatorDTO();

    if (communicationEntity.getCommunicatorId() != STANDARD_CLIENT_ID) {
      this.guiDataModel.setActiveClient(communicationEntity);
    }
  }

  public void resetGUIData() {
    this.guiDataModel.resetAllData();
  }

  public void startGUI() {
    this.guiDataModel.setGUILabelChangeListener(this.gui);
    this.threadPool.execute(this.gui);
    this.guiLogic.navigate(INITIAL);
  }

  // -------------------------------------------
  // ---Helper-Methods--------------------------
  // -------------------------------------------
}
