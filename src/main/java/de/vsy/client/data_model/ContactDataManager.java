/*
 *
 */
package de.vsy.client.data_model;

import de.vsy.shared_transmission.dto.CommunicatorDTO;
import de.vsy.shared_transmission.packet.content.relation.EligibleContactEntity;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Manages the client dataManagement for all active contacts
 *
 * <p>Frederic Heath
 */
public class ContactDataManager {

  private final Map<EligibleContactEntity, Set<CommunicatorDTO>> activeContactMap;

  public ContactDataManager() {
    this(new HashMap<>(10));
  }

  /**
   * Instantiates a new active contact controller.
   *
   * @param activeContactMap the active client list
   * @throws NullPointerException if no map is passed as argument
   */
  public ContactDataManager(
      final Map<EligibleContactEntity, Set<CommunicatorDTO>> activeContactMap) {

    if (activeContactMap == null) {
      throw new NullPointerException("Contact map null.");
    }
    this.activeContactMap = activeContactMap;
  }

  /**
   * Adds the active contact.
   *
   * @param newClient the new client
   */
  public void addContact(final EligibleContactEntity contactType, final CommunicatorDTO newClient) {
    var contactSet = this.activeContactMap.get(contactType);

    if (contactSet != null) {
      contactSet.add(newClient);
      this.activeContactMap.put(contactType, contactSet);
    }
  }

  /**
   * Gets the active contact list.
   *
   * @return the active contact list
   */
  public Set<CommunicatorDTO> getActiveContactList() {
    Set<CommunicatorDTO> activeContactList = new HashSet<>();

    for (var currentContactEntrySet : this.activeContactMap.entrySet()) {
      activeContactList.addAll(currentContactEntrySet.getValue());
    }
    return activeContactList;
  }

  /**
   * Removes the active contact.
   *
   * @param contactType the contact type
   * @param contactData the contact dataManagement
   */
  public void removeContact(
      final EligibleContactEntity contactType, final CommunicatorDTO contactData) {
    var contactRemoved = false;
    final var contactSet = this.activeContactMap.get(contactType);

    if (contactSet != null) {
      contactSet.remove(contactData);
      this.activeContactMap.put(contactType, contactSet);
    }
  }

  /**
   * Lists the new client list.
   *
   * @param activeContactMap the new client list
   */
  public void setNewClientList(
      final Map<EligibleContactEntity, Set<CommunicatorDTO>> activeContactMap) {
    resetContactList();

    if (!activeContactMap.isEmpty()) {
      final Map<EligibleContactEntity, Set<CommunicatorDTO>> newActiveClients =
          new HashMap<>(activeContactMap.size());
      newActiveClients.putAll(activeContactMap);
      this.activeContactMap.putAll(newActiveClients);
    }
  }

  public void resetContactList() {
    this.activeContactMap.clear();
  }
}
