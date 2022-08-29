package de.vsy.chat.client.data_model;

import static de.vsy.chat.utility.standard_value.StandardIdProvider.STANDARD_CLIENT_ID;
import static de.vsy.chat.utility.standard_value.StandardStringProvider.STANDARD_EMTPY_STRING;

import de.vsy.chat.transmission.dto.CommunicatorDTO;
import de.vsy.chat.transmission.dto.authentication.AuthenticationDTO;

/** The Class ClientDataManager. */
public class ClientDataManager {

  private AuthenticationDTO authenticationData;
  private CommunicatorDTO personalData;

  /** Instantiates a new client account dataManagement. */
  public ClientDataManager() {
    this(
        AuthenticationDTO.valueOf(STANDARD_EMTPY_STRING, STANDARD_EMTPY_STRING),
        CommunicatorDTO.valueOf(STANDARD_CLIENT_ID, STANDARD_EMTPY_STRING));
  }

  /**
   * Instantiates a new client account dataManagement.
   *
   * @param authData the auth dataManagement
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

    if (authData != null) {
      this.authenticationData = authData;
    } else {
      this.authenticationData =
          AuthenticationDTO.valueOf(STANDARD_EMTPY_STRING, STANDARD_EMTPY_STRING);
    }
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

    if (clientData != null) {
      this.personalData = clientData;
    } else {
      this.personalData = CommunicatorDTO.valueOf(STANDARD_CLIENT_ID, STANDARD_EMTPY_STRING);
    }
  }
}
