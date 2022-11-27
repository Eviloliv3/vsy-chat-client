package de.vsy.client.packet_processing;

import de.vsy.client.data_model.notification.SimpleInformation;
import de.vsy.shared_transmission.packet.content.Translatable;

public class SimpleInformationTranslator {
  public static String translate(Translatable simpleNotification){
    if(simpleNotification instanceof final SimpleInformation simpleInfo){
        return simpleInfo.getInformationString();
    }
    return null;
  }
}
