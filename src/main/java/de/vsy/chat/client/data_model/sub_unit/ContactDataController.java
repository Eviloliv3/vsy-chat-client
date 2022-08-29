/*
 *
 */
package de.vsy.chat.client.data_model.sub_unit;

import de.vsy.chat.transmission.dto.CommunicatorDTO;
import de.vsy.chat.transmission.packet.content.relation.EligibleContactEntity;

import java.util.*;

/**
 * Manages the client dataManagement for all active contacts
 *
 * <p>Frederic Heath
 */
public class ContactDataController {

  private final Map<EligibleContactEntity, Set<CommunicatorDTO>> activeContactMap;

  public ContactDataController() {
    this(new HashMap<>(10));
  }

  /**
   * Instantiates a new active contact controller.
   *
   * @param activeContactMap the active client list
   * @throws NullPointerException if no map is passed as argument
   */
  public ContactDataController(
      final Map<EligibleContactEntity, Set<CommunicatorDTO>> activeContactMap) {

    if (activeContactMap == null) {
      throw new NullPointerException("Es waren wurde keine Map aktiver Kontakte uebergeben.");
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
   * @return the active contact
   */
  public boolean removeContact(
      final EligibleContactEntity contactType, final CommunicatorDTO contactData) {
    var contactRemoved = false;
    final var contactSet = this.activeContactMap.get(contactType);

    if (contactSet != null) {
      contactRemoved = contactSet.remove(contactData);
      this.activeContactMap.put(contactType, contactSet);
    }
    return contactRemoved;
  }

  /**
   * Lists the new client list.
   *
   * @param activeContactMap the new new client list
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
