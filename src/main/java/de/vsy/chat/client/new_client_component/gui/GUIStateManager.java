/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package de.vsy.chat.client.new_client_component.gui;

import de.vsy.chat.client.gui.essential_graphical_units.MessageHistory;
import de.vsy.chat.client.gui.essential_graphical_units.dialog.ChatClientDialog;
import de.vsy.chat.transmission.dto.CommunicatorDTO;

import static de.vsy.chat.utility.standard_value.StandardIdProvider.STANDARD_CLIENT_ID;
import static de.vsy.chat.utility.standard_value.StandardStringProvider.STANDARD_EMTPY_STRING;

import java.util.LinkedList;
import java.util.Queue;
import javax.swing.DefaultListModel;

/** @author fredward */
public class GUIStateManager {

  private volatile boolean guiClosedFlag;
  private final MessageHistory messages;
  private final Queue<ChatClientDialog> pendingDialogQueue;
  private CommunicatorDTO activeChat;
  private CommunicatorDTO activeContact;
  private DefaultListModel<CommunicatorDTO> contactListModel;
  private ChatClientDialog activeDialog;

  public GUIStateManager() {
    this.guiClosedFlag = false;
    this.messages = new MessageHistory();
    pendingDialogQueue = new LinkedList<>();
    this.activeContact = CommunicatorDTO.valueOf(STANDARD_CLIENT_ID, STANDARD_EMTPY_STRING);
    this.activeChat = CommunicatorDTO.valueOf(STANDARD_CLIENT_ID, STANDARD_EMTPY_STRING);
    this.contactListModel = new DefaultListModel<>();
    activeDialog = null;
  }

  /**
   * Gets the GUI state flag.
   *
   * @return the GUI state flag
   */
  // -------------------------------------------
  public boolean getGUIStateFlag() {
    return this.guiClosedFlag;
  }

  /**
   * Sets the GUI state flag.
   *
   * @param newState the new GUI state flag
   */
  public void setGUIStateFlag(final boolean newState) {
    this.guiClosedFlag = newState;
  }
}
