package de.vsy.chat.client.new_client_component.data_model;

import de.vsy.chat.client.data_model.ClientDataManager;
import de.vsy.chat.transmission.dto.CommunicatorDTO;
import de.vsy.chat.transmission.packet.content.Translatable;
import de.vsy.chat.transmission.packet.content.chat.TextMessageDTO;
import de.vsy.chat.transmission.packet.content.relation.EligibleContactEntity;

import static de.vsy.chat.utility.standard_value.StandardIdProvider.STANDARD_CLIENT_ID;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** @author fredward */
public class DataModel {

  private final Logger logger;
  private final Map<EligibleContactEntity, Set<CommunicatorDTO>> activeContactMap;
  private final Map<Integer, List<TextMessageDTO>> oldMessages;
  private final Queue<Translatable> pendingDialogQueue;
  private final ClientDataManager clientData;

  {
    this.logger = LogManager.getLogger();
    this.activeContactMap = new HashMap<>();
    this.oldMessages = new HashMap<>();
    this.pendingDialogQueue = new LinkedList<>();
    this.clientData = new ClientDataManager();
  }

  public DataModel () {}
  
  public void addDialogContent(Translatable dialogContent){
    this.pendingDialogQueue.add(dialogContent);
  }
  
  public Translatable getNextDialogContent(){
    return this.pendingDialogQueue.poll();
  }
  
  /**
   * Lists the client dataManagement.
   *
   * @param client the new client dataManagement
   */
  public void setCommunicatorDTO(final CommunicatorDTO client) {
    this.clientData.setCommunicatorDTO(client);
  }
  
  /**
   * Gets the client account dataManagement.
   *
   * @return the client account dataManagement
   */
  public ClientDataManager getClientAccountData() {
    return this.clientData;
  }
  
  public boolean clientIsAuthenticated(){
    return this.clientData.getCommunicatorDTO().getCommunicatorId() != STANDARD_CLIENT_ID;
  }
  
  public void resetModel(){
    
  }
}
