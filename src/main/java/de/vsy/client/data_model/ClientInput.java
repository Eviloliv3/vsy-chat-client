package de.vsy.client.data_model;

public class ClientInput<T> {

  private final T clientMessage;
  private final int contactId;

  public ClientInput(final T input, final int recipientId) {
    this.clientMessage = input;
    this.contactId = recipientId;
  }

  public T getClientMessage() {
    return this.clientMessage;
  }

  public int getContactId() {
    return this.contactId;
  }
}
