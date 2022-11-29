/*
 *
 */
package de.vsy.client.gui.chatter_main_model;

import static de.vsy.client.gui.essential_graphical_units.prompt.NavigationGoal.INITIAL;
import static de.vsy.shared_transmission.packet.property.communicator.CommunicationEndpoint.getClientEntity;
import static de.vsy.shared_transmission.packet.property.communicator.CommunicationEndpoint.getServerEntity;
import static de.vsy.shared_utility.standard_value.StandardIdProvider.STANDARD_CLIENT_ID;
import static de.vsy.shared_utility.standard_value.StandardIdProvider.STANDARD_SERVER_ID;

import de.vsy.client.controlling.essential_gui_action_interfaces.DialogInitNavigation;
import de.vsy.client.controlling.essential_gui_action_interfaces.guiActionInterfaces.GUIChatActions;
import de.vsy.client.controlling.essential_gui_action_interfaces.guiActionInterfaces.GUIEssentialActions;
import de.vsy.client.data_model.GUIStateManager;
import de.vsy.client.data_model.ServerDataCache;
import de.vsy.client.data_model.notification.SimpleInformation;
import de.vsy.client.gui.essential_graphical_units.IntroductionActionListener;
import de.vsy.client.gui.essential_graphical_units.prompt.AccountCreationPanel;
import de.vsy.client.gui.essential_graphical_units.prompt.ContactAdditionPanel;
import de.vsy.client.gui.essential_graphical_units.prompt.LoginPanel;
import de.vsy.client.gui.essential_graphical_units.prompt.NavigationGoal;
import de.vsy.client.gui.essential_graphical_units.prompt.WelcomeDialog;
import de.vsy.client.gui.utility.ComponentInputRemover;
import de.vsy.client.packet_processing.RequestPacketCreator;
import de.vsy.shared_transmission.dto.CommunicatorDTO;
import de.vsy.shared_transmission.dto.authentication.AccountCreationDTO;
import de.vsy.shared_transmission.dto.authentication.AuthenticationDTO;
import de.vsy.shared_transmission.dto.authentication.PersonalData;
import de.vsy.shared_transmission.packet.content.PacketContent;
import de.vsy.shared_transmission.packet.content.authentication.LoginRequestDTO;
import de.vsy.shared_transmission.packet.content.authentication.LogoutRequestDTO;
import de.vsy.shared_transmission.packet.content.authentication.NewAccountRequestDTO;
import de.vsy.shared_transmission.packet.content.chat.TextMessageDTO;
import de.vsy.shared_transmission.packet.content.relation.ContactRelationRequestDTO;
import de.vsy.shared_transmission.packet.content.relation.EligibleContactEntity;
import de.vsy.shared_transmission.packet.content.status.ContactMessengerStatusDTO;
import de.vsy.shared_transmission.packet.property.communicator.CommunicationEndpoint;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;

public class GUIInteractionProcessor
    implements GUIChatActions, GUIEssentialActions,
    DialogInitNavigation {

  private final GUIStateManager guiDataModel;
  private final GUILogicRelevantDataProvider guiLiveData;
  private final RequestPacketCreator requester;
  private final ServerDataCache serverDataModel;

  /**
   * Instantiates a new GUI logic.
   *
   * @param guiDataProvider the gui dataManagement provider
   * @param serverData      the server dataManagement
   * @param guiData         the gui dataManagement
   * @param requester       the requester
   */
  public GUIInteractionProcessor(
      final GUILogicRelevantDataProvider guiDataProvider,
      final ServerDataCache serverData,
      final GUIStateManager guiData,
      final RequestPacketCreator requester) {
    this.guiLiveData = guiDataProvider;
    this.serverDataModel = serverData;
    this.guiDataModel = guiData;
    this.requester = requester;
  }

  public void handleContactRemoval() {
    final var contactData = this.guiLiveData.getSelectedContact();

    if (contactData != null) {
      final var contactId = contactData.getCommunicatorId();
      this.requester.request(
          new ContactRelationRequestDTO(
              EligibleContactEntity.CLIENT,
              this.serverDataModel.getClientId(),
              contactId, this.serverDataModel.getCommunicatorData(),
              false),
          getClientEntity(contactId));
    } else {
      final var errorMessage = "Relationship was not upended. No contact list entry selected";
      this.serverDataModel.addNotification(new SimpleInformation(errorMessage));
    }
  }

  /**
   * Choose contact.
   *
   * @param evt the evt
   */
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
          new TextMessageDTO(this.serverDataModel.getClientId(), EligibleContactEntity.CLIENT,
              contactId, message),
          getClientEntity(contactId));
    }
  }

  /**
   * Show dialog.
   */
  private void showWelcomeDialog() {
    IntroductionActionListener ac = new IntroductionActionListener(this);
    final var welcomeDialog = new WelcomeDialog(ac);
    welcomeDialog.setLocationRelativeTo(null);
    welcomeDialog.setVisible(true);
  }

  public void handleLogout() {

    if (this.serverDataModel.getClientId() != STANDARD_CLIENT_ID) {
      this.requester.request(
          new ContactMessengerStatusDTO(
              EligibleContactEntity.CLIENT,
              false,
              this.serverDataModel.getCommunicatorData(),
              null),
          getServerEntity(STANDARD_SERVER_ID));
      this.requester.request(
          new LogoutRequestDTO(this.serverDataModel.getCommunicatorData()),
          getServerEntity(STANDARD_SERVER_ID));
    }
  }

  @Override
  public void endProgram() {
    closeApplication();
  }

  @Override
  public void closeApplication() {
    handleLogout();
  }

  /**
   * Navigate.
   *
   * @param goal the navigation goal
   */
  @Override
  public void navigate(final NavigationGoal goal) {
    switch (goal) {
      case INITIAL -> showWelcomeDialog();
      case LOGIN -> handleLogin();
      case ACCOUNT_CREATION -> handleAccountCreation();
      case LOGOUT -> handleLogout();
      case CONTACT_REMOVAL -> handleContactRemoval();
      case CONTACT_ADDITION -> handleContactAddition();
    }
  }

  //TODO Accounterstellung Username falsch angeordnet
  private void handleLogin() {
    final var notLoggedIn = this.serverDataModel.getClientAccountData().clientNotLoggedIn();

    if (notLoggedIn) {
      final var loginPanel = new LoginPanel();
      final String[] options = {"Login", "Cancel"};
      final var loginOption = JOptionPane.showOptionDialog(null, loginPanel, "Enter credentials",
          JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

      if (loginOption == JOptionPane.OK_OPTION) {
        final PacketContent data;
        final String username;
        char[] passwordChars;

        username = loginPanel.getUsername();
        passwordChars = loginPanel.getPassword();

        if (username.length() < 5) {
          this.serverDataModel.addPriorityNotification(new SimpleInformation(
              "Retry: username length has to be greater than 5, provided " + username.length()));
          handleLogin();
        } else if (passwordChars.length < 5) {
          this.serverDataModel.addPriorityNotification(new SimpleInformation(
              "Retry: password length has to be greater than 5, provided " + passwordChars.length));
          handleLogin();
        } else {
          //var hashedPassword = PasswordHasher.calculateHash(passwordChars);
          this.requester.request(new LoginRequestDTO(username, String.valueOf(passwordChars)), getServerEntity(STANDARD_SERVER_ID));
        }
      } else {
        ComponentInputRemover.clearInput(loginPanel);
        navigate(INITIAL);
      }
    } else {
      final var notification = new SimpleInformation("Bitte melden Sie sich zuerst ab.");
      this.serverDataModel.addNotification(notification);
    }
  }

  private void handleAccountCreation() {
    final var noLoggedIn = this.serverDataModel.getClientAccountData().clientNotLoggedIn();

    if (noLoggedIn) {

      var accountCreationPanel = new AccountCreationPanel();
      String[] options = {"Create", "Cancel"};
      final var creationOption = JOptionPane.showOptionDialog(null, accountCreationPanel,
          "Account Creation", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
          options, options[0]);
      //TODO string checks
      if (creationOption == JOptionPane.OK_OPTION) {
        final PacketContent data;
        var firstName = accountCreationPanel.getFirstName();
        var lastName = accountCreationPanel.getLastName();
        //var password = PasswordHasher.calculateHash(accountCreationPanel.getPassword());
        var password = String.valueOf(accountCreationPanel.getPassword());
        var username = accountCreationPanel.getLogin();

        this.requester.request(new NewAccountRequestDTO(
            new AccountCreationDTO(AuthenticationDTO.valueOf(username, password),
                PersonalData.valueOf(firstName, lastName))), getServerEntity(STANDARD_SERVER_ID));
      } else {
        ComponentInputRemover.clearInput(accountCreationPanel);
        this.navigate(INITIAL);
      }
    } else {
      final var notification = new SimpleInformation("Bitte melden Sie sich zuerst ab.");
      this.serverDataModel.addNotification(notification);
    }
  }

  private void handleContactAddition() {
    var contactAdd = new ContactAdditionPanel();
    String[] options = {"Request", "Cancel"};

    final var contactAdditionOption = JOptionPane.showOptionDialog(null, contactAdd,
        "Contact request", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options,
        options[0]);

    if (contactAdditionOption == JOptionPane.OK_OPTION) {
      int contactId;

      try {
        contactId = contactAdd.getContactId();
      } catch (NumberFormatException nfe) {
        this.serverDataModel.addPriorityNotification(
            new SimpleInformation("Input cannot be parsed to a number."));
        this.handleContactAddition();
        return;
      }

      if (contactId < 0) {
        this.serverDataModel.addNotification(new SimpleInformation("Negative ids do not exist."));
      } else {
        this.requester.request(new ContactRelationRequestDTO(EligibleContactEntity.CLIENT,
            STANDARD_CLIENT_ID, contactId, this.serverDataModel.getClientAccountData()
            .getCommunicatorDTO(), true), getClientEntity(contactId));
      }
    } else {
      ComponentInputRemover.clearInput(contactAdd);
      this.navigate(INITIAL);
    }
  }
}
