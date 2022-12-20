/*
 *
 */
package de.vsy.client.data_model;

import de.vsy.shared_transmission.dto.CommunicatorDTO;
import de.vsy.shared_transmission.packet.content.relation.EligibleContactEntity;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Manages the client dataManagement for all active contacts
 *
 * <p>Frederic Heath
 */
public class ContactDataManager {

  private static final Logger LOGGER = LogManager.getLogger();
  private final Map<EligibleContactEntity, List<CommunicatorDTO>> activeContactMap;
  private final List<CommunicatorDTO> indexList;

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
      final Map<EligibleContactEntity, List<CommunicatorDTO>> activeContactMap) {

    if (activeContactMap == null) {
      throw new NullPointerException("Contact map null.");
    }
    this.activeContactMap = activeContactMap;
    this.indexList = new LinkedList<>();
    activeContactMap.values().forEach((contactList) -> contactList.forEach(this.indexList::add));
  }

  /**
   * Adds the active contact.
   *
   * @param contactType the contact type
   * @param contact     the new client
   */
  public int addContact(final EligibleContactEntity contactType, final CommunicatorDTO contact) {
    int contactIndex = -1;
    var contactList = this.activeContactMap.getOrDefault(contactType, new LinkedList<>());

    if (!(indexList.contains(contact))) {
      contactList.add(contact);
      this.activeContactMap.put(contactType, contactList);

      indexList.add(contact);
      CommunicatorDataSorter.sortByFirstLastName(indexList);
      contactIndex = indexList.indexOf(contact);
    } else {
      LOGGER.warn("Contact not added - already contained.");
    }
    return contactIndex;
  }

  /**
   * Gets the active contact list.
   *
   * @return the active contact list
   */
  public List<CommunicatorDTO> getActiveContactList() {
    return Collections.unmodifiableList(this.indexList);
  }

  /**
   * Removes the active contact.
   *
   * @param contactType the contact type
   * @param contact     the contact dataManagement
   */
  public void removeContact(
      final EligibleContactEntity contactType, final CommunicatorDTO contact) {
    var contactRemoved = false;
    final var contactList = this.activeContactMap.getOrDefault(contactType, new LinkedList<>());

    contactList.remove(contact);
    this.activeContactMap.put(contactType, contactList);
    this.indexList.remove(contact);
  }

  /**
   * Lists the new client list.
   *
   * @param activeContactMap the new client list
   */
  public void setNewClientList(
      final Map<EligibleContactEntity, List<CommunicatorDTO>> activeContactMap) {

    if (!activeContactMap.isEmpty()) {
      resetContactList();
      this.activeContactMap.putAll(activeContactMap);
      activeContactMap.values().forEach((contactList) -> contactList.forEach(this.indexList::add));
    }
  }

  public void resetContactList() {
    this.activeContactMap.clear();
    this.indexList.clear();
  }

  public CommunicatorDTO getContactData(int contactId) {

    for (var currentContact : this.indexList) {
      if (currentContact.getCommunicatorId() == contactId) {
        return currentContact;
      }
    }
    return null;
  }
}
