package de.vsy.client.data_model;

import static de.vsy.shared_utility.standard_value.StandardIdProvider.STANDARD_CLIENT_ID;
import static de.vsy.shared_utility.standard_value.StandardStringProvider.STANDARD_EMPTY_STRING;

import de.vsy.shared_transmission.dto.CommunicatorDTO;
import de.vsy.shared_transmission.dto.authentication.AuthenticationDTO;
import java.util.Objects;

/**
 * The Class ClientDataManager.
 */
public class ClientDataManager {

  private AuthenticationDTO authenticationData;
  private CommunicatorDTO personalData;

  /**
   * Instantiates a new client account dataManagement.
   */
  public ClientDataManager() {
    this(
        AuthenticationDTO.valueOf(STANDARD_EMPTY_STRING, STANDARD_EMPTY_STRING),
        CommunicatorDTO.valueOf(STANDARD_CLIENT_ID, STANDARD_EMPTY_STRING));
  }

  /**
   * Instantiates a new client account dataManagement.
   *
   * @param authData   the auth dataManagement
   * @param clientData the client dataManagement
   */
  public ClientDataManager(final AuthenticationDTO authData, final CommunicatorDTO clientData) {
    this.authenticationData = authData;
    this.personalData = clientData;
  }

  /**
   * Gets the authentication dataManagement.
   *
   * @return the authentication dataManagement
   */
  public AuthenticationDTO getAuthenticationDTO() {
    return this.authenticationData;
  }

  /**
   * Sets the authentication dataManagement.
   *
   * @param authData the new authentication dataManagement
   */
  public void setAuthenticationDTO(final AuthenticationDTO authData) {

    this.authenticationData = Objects.requireNonNullElseGet(authData,
        () -> AuthenticationDTO.valueOf(STANDARD_EMPTY_STRING, STANDARD_EMPTY_STRING));
  }

  /**
   * Gets the communicator dataManagement.
   *
   * @return the communicator dataManagement
   */
  public CommunicatorDTO getCommunicatorDTO() {
    return this.personalData;
  }

  /**
   * Sets the communicator dataManagement.
   *
   * @param clientData the new communicator dataManagement
   */
  public void setCommunicatorDTO(final CommunicatorDTO clientData) {

    this.personalData = Objects.requireNonNullElseGet(clientData,
        () -> CommunicatorDTO.valueOf(STANDARD_CLIENT_ID, STANDARD_EMPTY_STRING));
  }

  public boolean clientNotLoggedIn() {
    return this.personalData.getCommunicatorId() == STANDARD_CLIENT_ID;
  }
}
