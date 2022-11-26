package de.vsy.client.data_model.notification;

import de.vsy.shared_transmission.packet.content.Translatable;

public class SimpleInformation implements Translatable {

  private final String informationString;

  public SimpleInformation(final String info) {
    this.informationString = info;
  }

  public String getInformationString() {
    return this.informationString;
  }
}
