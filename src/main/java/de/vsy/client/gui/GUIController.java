/*
 *
 */
package de.vsy.client.gui;

import static de.vsy.client.gui.essential_graphical_unit.NavigationGoal.INITIAL;
import static de.vsy.shared_utility.standard_value.StandardIdProvider.STANDARD_CLIENT_ID;
import static java.util.concurrent.Executors.newSingleThreadExecutor;

import de.vsy.client.data_model.ServerDataCache;
import de.vsy.client.gui.essential_graphical_unit.MenuActionListener;
import de.vsy.shared_transmission.dto.CommunicatorDTO;
import de.vsy.shared_transmission.packet.content.chat.TextMessageDTO;
import de.vsy.shared_utility.id_manipulation.IdComparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.SwingUtilities;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Basic Controller for initiating and destroying the GUI and the appertaining logic.
 */
public class GUIController {

  private static final Logger LOGGER = LogManager.getLogger();
  private final ClientChatGUI gui;
  private final GUIInteractionProcessor guiInteractions;
  private final ServerDataCache serverDataModel;
  private final ExecutorService guiExecutor;

  /**
   * Instantiates a new GUI controller.
   *
   * @param gui             the gui
   * @param serverData      the server dataManagement
   * @param guiInteractions the gui interactions
   */
  public GUIController(
      final ClientChatGUI gui,
      final ServerDataCache serverData,
      final GUIInteractionProcessor guiInteractions) {

    this.guiExecutor = newSingleThreadExecutor();
    this.gui = gui;
    this.serverDataModel = serverData;
    this.guiInteractions = guiInteractions;
  }

  public void closeController() {
    this.guiExecutor.shutdownNow();
    try {
      var guiTerminated = this.guiExecutor.awaitTermination(5000, TimeUnit.MILLISECONDS);

      if (guiTerminated) {
        LOGGER.trace("GUI successfully shutdown.");
      } else {
        LOGGER.error("GUI shutdown unexpectedly took more than 5 second and may be deadlocked.");
      }
    } catch (final InterruptedException e) {
      Thread.currentThread().interrupt();
      LOGGER.error("Interrupted while waiting for gui thread to shutdown.");
    }
  }

  /**
   * Contact status change.
   *
   * @param contactIndex the contact list index
   * @param contactData  the contact data
   */
  public void contactStatusChange(final int contactIndex, final CommunicatorDTO contactData) {

    if (contactIndex > -1) {
      this.gui.addContact(contactIndex, contactData);
    } else {
      this.gui.removeContact(contactData);
    }
  }

  public void addClientTitle(final CommunicatorDTO clientData) {
    final String titleLabel;
    final var clientId = clientData.getCommunicatorId();

    if (clientId != STANDARD_CLIENT_ID) {
      final var clientUsername = clientData.getDisplayLabel();
      titleLabel = String.format("You are %s#%d)", clientUsername, clientId);
    } else {
      titleLabel = "Not authenticated";
    }
    SwingUtilities.invokeLater(() -> this.gui.setClientTitle(titleLabel));
  }

  /**
   * Adds the message.
   *
   * @param message the message
   */
  public void addMessage(final CommunicatorDTO contact, final TextMessageDTO message) {
    final var contactName = contact.getDisplayLabel();
    final var clientId = this.serverDataModel.getClientId();
    final var clientBoundMessage = IdComparator.determineIfOriginator(clientId,
        message.getOriginatorId());

    this.gui.addMessage(contactName, clientBoundMessage, message);
  }

  public void addContactsToGui() {
    var contactList = this.serverDataModel.getContactList();

    for (var contact : contactList) {
      this.gui.addContact(0, contact);
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

  public void resetGUIData() {
    this.gui.resetData();
    this.guiInteractions.navigate(INITIAL);
  }

  public void startGUI() {
    prepareGUI();

    SwingUtilities.invokeLater(() -> {
      var currentGUI = GUIController.this.gui;
      currentGUI.validate();
      currentGUI.pack();
      currentGUI.setLocationRelativeTo(null);
      currentGUI.setVisible(true);
      guiInteractions.navigate(INITIAL);
    });
  }

  private void prepareGUI() {
    this.gui.addInteractionLogic(this.guiInteractions,
        new MenuActionListener(this.guiInteractions));
    try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        if ("FlatLaf Dark".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException |
             InstantiationException ex) {
      java.util.logging.Logger.getLogger(ClientChatGUI.class.getName())
          .log(java.util.logging.Level.SEVERE, null, ex);
    }
  }
}
