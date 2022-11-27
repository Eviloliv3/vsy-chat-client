package de.vsy.client.packet_processing;

import de.vsy.client.data_model.notification.SimpleInformation;
import de.vsy.client.packet_processing.content_processing.SimpleInformationTranslator;
import de.vsy.shared_module.packet_content_translation.NotificationTranslator;
import de.vsy.shared_transmission.packet.content.Translatable;

public class ClientNotificationTranslator extends NotificationTranslator {
  static{
    translators.put(SimpleInformation.class, SimpleInformationTranslator::translate);
  }
    protected ClientNotificationTranslator(){}

  public static String translate(Translatable request){
    final var translator = translators.get(request.getClass());
    if (translator != null) {
      return translator.apply(request);
    }
    return NotificationTranslator.translate(request);
  }
}
