package de.vsy.chat.client.new_client_component.gui.logic;

import de.vsy.chat.client.controlling.essential_gui_action_interfaces.guiActionInterfaces.GUIChatActions;
import de.vsy.chat.client.new_client_component.data_model.DataModel;
import de.vsy.chat.client.new_client_component.gui.GUIBaseFrame;
import de.vsy.chat.transmission.dto.CommunicatorDTO;
import de.vsy.chat.transmission.packet.content.chat.TextMessageDTO;
import java.awt.event.MouseEvent;
import java.util.List;

/** @author fredward */
public class GUIBaseFrameLogic implements GUIChatActions {
  private DataModel dataAccess;
  private GUIBaseFrame gui;

  @Override
  public void chooseContact(final MouseEvent evt) {

    if (evt.getClickCount() == 2) {
      CommunicatorDTO contact;
      List<TextMessageDTO> messages;

      contact = this.gui.getSelectedContact();
      messages = this.dataAccess.getMessageList(contact.getCommunicatorId());

      this.gui.setNewActiveChat(contact, messages);
    }
  }

  @Override
  public void sendMessage() {

    final var message = this.gui.getMessage();
    final var activeChat = this.gui.getActiveChatContact();

    if (message != null && !message.isBlank()) {
      final var contactId = activeChat.getCommunicatorId();
      this.requester.request(
          new TextMessageDTO(this.dataAccess.getCommunicatorId(), contactId, message),
          contactId);
    }
  }
}
