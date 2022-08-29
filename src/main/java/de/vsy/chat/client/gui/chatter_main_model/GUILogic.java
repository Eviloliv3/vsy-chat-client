/*
 *
 */
package de.vsy.chat.client.gui.chatter_main_model;

import de.vsy.chat.client.controlling.essential_gui_action_interfaces.DialogEssentialActions;
import de.vsy.chat.client.controlling.essential_gui_action_interfaces.DialogInitNavigation;
import de.vsy.chat.client.controlling.essential_gui_action_interfaces.GUIActions;
import de.vsy.chat.client.controlling.essential_gui_action_interfaces.GUIMenuActions;
import de.vsy.chat.client.controlling.packet_creation.RequestPacketCreator;
import de.vsy.chat.client.data_model.GUIStateDataManager;
import de.vsy.chat.client.data_model.ServerProvidedDataManager;
import de.vsy.chat.transmission.dto.CommunicatorDTO;

import static de.vsy.chat.client.gui.essential_graphical_units.dialog.DialogStatus.ACCOUNT_CREATION;
import static de.vsy.chat.client.gui.essential_graphical_units.dialog.DialogStatus.LOGIN;
import static de.vsy.chat.utility.standard_value.StandardIdProvider.STANDARD_CLIENT_ID;
import static de.vsy.chat.utility.standard_value.StandardIdProvider.STANDARD_SERVER_ID;

import de.vsy.chat.client.gui.essential_graphical_units.dialog.*;
import de.vsy.chat.client.gui.external_gui_access_interfaces.CloseApplicationFlagProvider;
import de.vsy.chat.transmission.packet.content.authentication.LogoutRequestDTO;
import de.vsy.chat.transmission.packet.content.chat.TextMessageDTO;
import de.vsy.chat.transmission.packet.content.error.ErrorDTO;
import de.vsy.chat.transmission.packet.content.relation.ContactRelationRequestDTO;
import de.vsy.chat.transmission.packet.content.relation.EligibleContactEntity;
import de.vsy.chat.transmission.packet.content.status.ContactMessengerStatusDTO;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.List;

public class GUILogic
    implements GUIActions,
        GUIMenuActions,
        DialogEssentialActions,
        DialogInitNavigation,
        CloseApplicationFlagProvider {

  private final GUIStateDataManager guiDataModel;
  private final GUILogicRelevantDataProvider guiLiveData;
  private final RequestPacketCreator requester;
  private final ServerProvidedDataManager serverDataModel;

  /**
   * Instantiates a new GUI logic.
   *
   * @param guiDataProvider the gui dataManagement provider
   * @param serverData the server dataManagement
   * @param guiData the gui dataManagement
   * @param requester the requester
   */
  public GUILogic(
      final GUILogicRelevantDataProvider guiDataProvider,
      final ServerProvidedDataManager serverData,
      final GUIStateDataManager guiData,
      final RequestPacketCreator requester) {
    this.guiLiveData = guiDataProvider;
    this.serverDataModel = serverData;
    this.guiDataModel = guiData;
    this.requester = requester;
  }

  // -------------------------------------------
  // ---GUI-Menu-Actions------------------------

  // -------------------------------------------
  @Override
  public void addContact() {
    final ChatClientDialog newDialog = new ContactAdditionDialog(this);
    showDialog(newDialog);
  }

  // -------------------------------------------
  // ---GUI-Actions-----------------------------

  @Override
  public void removeContact() {
    final var contactData = this.guiLiveData.getSelectedContact();

    if (contactData != null) {
      final var contactId = contactData.getCommunicatorId();
      this.requester.request(
          new ContactRelationRequestDTO(
              EligibleContactEntity.CLIENT,
              this.serverDataModel.getCommunicatorId(),
              contactId,
              false),
          contactId);
    } else {
      final var errorMessage = "Keine Freundschaft wurde beendet.";
      final var errorCause = "Es ist kein Eintrag in der Kontaktliste ausgewaehlt.";
      this.serverDataModel.addDialogMessage(new ErrorDTO(errorMessage + errorCause, null));
    }
  }

  /**
   * Show dialog.
   *
   * @param newDialog the new dialog
   * @return true, if successful
   */
  private boolean showDialog(final ChatClientDialog newDialog) {

    if (newDialog != null && this.guiDataModel.addActiveDialog(newDialog)) {
      newDialog.setLocationRelativeTo(null);
      newDialog.setVisible(true);
      return true;
    }
    return false;
  }

  /**
   * Choose contact.
   *
   * @param evt the evt
   */
  // -------------------------------------------
  @Override
  public void chooseContact(final MouseEvent evt) {

    if (evt.getClickCount() == 2) {
      CommunicatorDTO contact;
      List<TextMessageDTO> messages;

      contact = this.guiLiveData.getSelectedContact();
      messages = this.serverDataModel.getMessageList(contact.getCommunicatorId());

      this.guiDataModel.setNewActiveChat(contact, messages);
    }
  }

  @Override
  public void sendMessage() {

    final var message = this.guiLiveData.getMessage();
    final var activeChat = this.guiDataModel.getActiveChatContact();

    if (message != null && !message.isBlank()) {
      final var contactId = activeChat.getCommunicatorId();
      this.requester.request(
          new TextMessageDTO(this.serverDataModel.getCommunicatorId(), contactId, message),
          contactId);
    }
  }

  @Override
  public void createAccount() {

    if (this.serverDataModel.getCommunicatorId() == STANDARD_CLIENT_ID) {
      navigate(ACCOUNT_CREATION);
    } else {
      this.serverDataModel.addDialogMessage("Bitte melden Sie sich zuerst ab.");
    }
  }

  // -------------------------------------------
  // ---Dialog-Actions--------------------------

  @Override
  public void closeDialog(final DialogType type) {
    clearDialog(type);
    this.guiDataModel.removeDialog(type);
  }

  // -------------------------------------------
  // ---Application-Termination-Flag------------

  /**
   * Evaluate dialog.
   *
   * @param type the type
   * @param contactId the contact id
   */
  // -------------------------------------------
  @Override
  public void evaluateDialog(final DialogType type, final int contactId) {
    final var dialog = this.guiDataModel.getDialog(type);

    if (dialog != null) {
      final var data = dialog.evaluate();

      if (data != null) {
        this.requester.request(data, contactId);
      }
    }
    closeDialog(type);

    if (dialog != null) {
      setupNextDialog(dialog.getDialogType());
    }
  }

  private void setupNextDialog(final DialogType type) {
    ChatClientDialog newDialog;

    newDialog = this.guiDataModel.getNextPendingDialog(type);

    if (newDialog == null) {
      final var dialogMessage = this.serverDataModel.getNextDialog(type);

      if (dialogMessage != null) {
        newDialog = createDialog(type, dialogMessage);
      }
    }
    showDialog(newDialog);
  }

  /**
   * Creates the dialog.
   *
   * @param type the type
   * @param message the message
   */
  // -------------------------------------------
  private ChatClientDialog createDialog(final DialogType type, final String message) {
    ChatClientDialog dialog = null;

    if (message != null) {

      switch (type) {
        case REQUEST:
          final var d = new RequestDialog(this);
          d.setRequest(this.serverDataModel.getNextRequest());
          dialog = d;
          break;
        case INFORMATION:
          dialog = new InformationDialog(this, message);
          break;
        case ERROR:
          final var d2 = new ErrorDialog(this);
          d2.setErrorMessage(message);
          dialog = d2;
          break;
        default:
          break;
      }
    }
    return dialog;
  }

  /**
   * Adds the to dialog queue.
   *
   * @param toAdd the to add
   */
  private void addToDialogQueue(final ChatClientDialog toAdd) {
    this.guiDataModel.addPendingDialog(toAdd);
  }

  // -------------------------------------------
  // ---Dialog-Navigation-----------------------

  /**
   * Clear dialog.
   *
   * @param type the type
   */
  private void clearDialog(final DialogType type) {
    final var toClear = this.guiDataModel.getDialog(type);

    if (toClear != null) {
      final var compList = toClear.getComponents();

      for (var i = compList.length - 1; i >= 0; i--) {

        if (compList[i] instanceof JTextField) {
          ((JTextField) compList[i]).setText("");
        } else if (compList[i] instanceof JTextArea) {
          ((JTextArea) compList[i]).setText("");
        }
      }
      toClear.dispose();
    }
  }

  @Override
  public void login() {

    if (this.serverDataModel.getCommunicatorId() == STANDARD_CLIENT_ID) {
      navigate(LOGIN);
    } else {
      this.serverDataModel.addDialogMessage("Bitte melden Sie sich zuerst ab.");
    }
  }

  @Override
  public void logout() {

    if (this.serverDataModel.getCommunicatorId() != STANDARD_CLIENT_ID) {
      this.requester.request(
          new ContactMessengerStatusDTO(
              EligibleContactEntity.CLIENT,
              false,
              this.serverDataModel.getClientAccountData().getCommunicatorDTO(),
              null),
          STANDARD_SERVER_ID);
      this.requester.request(
          new LogoutRequestDTO(this.serverDataModel.getClientAccountData().getCommunicatorDTO()),
          STANDARD_SERVER_ID);
    }
  }

  @Override
  public void endProgram() {
    closeApplication();
  }

  @Override
  public void closeApplication() {
    logout();
    setCloseFlag();
  }

  /**
   * Navigate.
   *
   * @param status the status
   */
  // -------------------------------------------
  @Override
  public void navigate(final DialogStatus status) {
    ChatClientDialog newDialog;

    switch (status) {
      case INITIAL:
        newDialog = new WelcomeDialog(this);
        break;
      case LOGIN:
        newDialog = new LoginDialog(this, this);
        break;
      case ACCOUNT_CREATION:
        newDialog = new AccountCreationDialog(this, this);
        break;
      default:
        newDialog =
            new ErrorDialog(
                this, new ErrorDTO("Dieser Status wird nicht unterstuetzt: " + status, null));
        break;
    }

    closeDialog(newDialog.getDialogType());

    if (!showDialog(newDialog)) {
      addToDialogQueue(newDialog);
    }
  }

  // -------------------------------------------
  // ---Helper-Methods--------------------------

  /**
   * Gets the close flag.
   *
   * @return the close flag
   */
  // -------------------------------------------
  @Override
  public boolean getCloseFlag() {
    return this.guiDataModel.getGUIStateFlag();
  }

  @Override
  public void setCloseFlag() {
    this.guiDataModel.setGUIStateFlag(true);
  }

  /**
   * Handle dialog request.
   *
   * @param type the type
   */
  public void handleDialogRequest(final DialogType type) {
    ChatClientDialog newDialog = null;
    final var oldDialog = this.guiDataModel.getDialog(type);

    if (oldDialog == null) {
      final var dialogMessage = this.serverDataModel.getNextDialog(type);

      if (dialogMessage != null) {
        newDialog = createDialog(type, dialogMessage);
      }
      showDialog(newDialog);
    } else {
      /*
       * Es gibt einen aktiven Dialog desselben Typs, deswegen reagiert
       * der Observer GUILogic nicht unmittelbar. Der Dialog wird
       * angezeigt, sobald er an der Reihe ist.
       */
    }
  }
}
