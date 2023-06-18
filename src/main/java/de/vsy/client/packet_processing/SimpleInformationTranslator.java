package de.vsy.client.packet_processing;

import de.vsy.shared_transmission.packet.content.Translatable;
import de.vsy.shared_transmission.packet.content.notification.SimpleInformationDTO;

public class SimpleInformationTranslator {

  public static String translate(Translatable simpleNotification) {
    if (simpleNotification instanceof final SimpleInformationDTO simpleInfo) {
      return simpleInfo.getInformationString();
    }
    return null;
  }
}
