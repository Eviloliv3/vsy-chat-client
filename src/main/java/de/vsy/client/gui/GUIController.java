/*
 *
 */
package de.vsy.client.gui;

import static de.vsy.client.gui.essential_graphical_units.prompt.NavigationGoal.INITIAL;
import static de.vsy.shared_utility.standard_value.StandardIdProvider.STANDARD_CLIENT_ID;
import static java.util.concurrent.Executors.newSingleThreadExecutor;

import de.vsy.client.data_model.GUIStateManager;
import de.vsy.client.data_model.ServerDataCache;
import de.vsy.client.gui.chatter_main_model.ClientChatGUI;
import de.vsy.client.gui.chatter_main_model.GUIInteractionProcessor;
import de.vsy.client.gui.essential_graphical_units.ChatterMenu;
import de.vsy.client.gui.essential_graphical_units.IntroductionActionListener;
import de.vsy.client.packet_processing.RequestPacketCreator;
import de.vsy.shared_transmission.dto.CommunicatorDTO;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Basic Controller for initiating and destroying the GUI and the appertaining logic.
 */
public class GUIController {

  private final Logger LOGGER = LogManager.getLogger();
  private final ClientChatGUI gui;
  private final GUIStateManager guiDataModel;
  private final GUIInteractionProcessor guiInteractions;
  private final ServerDataCache serverDataModel;
  private final ExecutorService guiExecutor;

  /**
   * Instantiates a new GUI controller.
   *
   * @param gui        the gui
   * @param serverData the server dataManagement
   * @param guiData    the gui dataManagement
   * @param requester  the requester
   */
  public GUIController(
      final ClientChatGUI gui,
      final ServerDataCache serverData,
      final GUIStateManager guiData,
      final RequestPacketCreator requester) {

    this.guiExecutor = newSingleThreadExecutor();
    this.gui = gui;
    this.serverDataModel = serverData;
    this.guiDataModel = guiData;
    this.guiInteractions = new GUIInteractionProcessor(this.gui, this.serverDataModel,
        this.guiDataModel, requester);
  }

  //TODO Variablen pruefen und umbennen
  public void closeController() {
    this.guiExecutor.shutdownNow();
    try {
      this.guiExecutor.awaitTermination(1000, TimeUnit.MILLISECONDS);
    } catch (final InterruptedException e) {
      Thread.currentThread().interrupt();
      LOGGER.error("Beim Warten auf das Schliessen aller Dialoge unterbrochen.");
    }
  }

  /**
   * Contact status change.
   *
   * @param contactData  the contact dataManagement
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
  public boolean guiNotTerminated() {
    return this.gui.isDisplayable();
  }

  public void initGUIControlling() {
    this.gui.initGUIControl(this.guiInteractions, this.guiInteractions,
        new ChatterMenu(new IntroductionActionListener(this.guiInteractions)));
    this.gui.setMessageHistory(this.guiDataModel.getActiveChatHistory());
    this.gui.setContactListModel(this.guiDataModel.getContactListModel());
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
    this.guiExecutor.execute(this.gui);
    this.guiInteractions.navigate(INITIAL);
  }
}
