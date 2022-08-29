package de.vsy.chat.client.new_client_component.gui.logic;

import static de.vsy.chat.utility.standard_value.StandardIdProvider.STANDARD_SERVER_ID;

import de.vsy.chat.client.controlling.essential_gui_action_interfaces.GUIMenuActions;
import de.vsy.chat.client.controlling.packet_creation.RequestPacketCreator;
import de.vsy.chat.client.gui.essential_graphical_units.dialog.AccountCreationDialog;
import de.vsy.chat.client.gui.essential_graphical_units.dialog.ChatClientDialog;
import de.vsy.chat.client.gui.essential_graphical_units.dialog.ContactAdditionDialog;
import de.vsy.chat.client.gui.essential_graphical_units.dialog.LoginDialog;
import de.vsy.chat.client.new_client_component.data_model.DataModel;
import de.vsy.chat.transmission.packet.content.authentication.LogoutRequestDTO;
import de.vsy.chat.transmission.packet.content.error.ErrorDTO;
import de.vsy.chat.transmission.packet.content.relation.ContactRelationRequestDTO;
import de.vsy.chat.transmission.packet.content.relation.EligibleContactEntity;
import de.vsy.chat.transmission.packet.content.status.ContactMessengerStatusDTO;

/** @author fredward */
public class ChatMenuBarLogic implements GUIMenuActions {

  private DataModel serverDataModel;
  private RequestPacketCreator requester;

  @Override
  public void createAccount() {
 
    if (this.serverDataModel.clientIsAuthenticated()) {
      ChatClientDialog dialog = new AccountCreationDialog(this, this);
    } else {
      this.serverDataModel.addDialogMessage("Bitte melden Sie sich zuerst ab.");
    }
  }

  @Override
  public void login() {

    if (this.serverDataModel.clientIsAuthenticated()) {
      ChatClientDialog dialog = new LoginDialog(this, this);
    } else {
      this.serverDataModel.addDialogMessage("Bitte melden Sie sich zuerst ab.");
    }
  }

  @Override
  public void logout() {

    if (!(this.serverDataModel.clientIsAuthenticated())) {
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
  public void addContact() {
    final ChatClientDialog newDialog = new ContactAdditionDialog(this);
    showDialog(newDialog);
  }

  @Override
  public void removeContact() {
    final var contactData = this.guiLiveData.getSelectedContact();

    if (contactData != null) {
      final var contactId = contactData.getCommunicatorId();
      this.requester.request(
          new ContactRelationRequestDTO(
              EligibleContactEntity.CLIENT,
              this.serverDataModel.getClientAccountData().getCommunicatorDTO().getCommunicatorId(),
              contactId,
              false),
          contactId);
    } else {
      final var errorMessage = "Keine Freundschaft wurde beendet.";
      final var errorCause = "Es ist kein Eintrag in der Kontaktliste ausgewaehlt.";
      this.serverDataModel.addDialogMessage(new ErrorDTO(errorMessage + errorCause, null));
    }
  }
}
