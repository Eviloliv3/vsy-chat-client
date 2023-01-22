package de.vsy.client.packet_processing;

import de.vsy.shared_transmission.packet.content.notification.SimpleInformationDTO;
import de.vsy.shared_module.packet_content_translation.NotificationTranslator;
import de.vsy.shared_transmission.packet.content.Translatable;

public class ClientNotificationTranslator extends NotificationTranslator {

  static {
    translators.put(SimpleInformationDTO.class, SimpleInformationTranslator::translate);
  }

  protected ClientNotificationTranslator() {
  }

  public static String translate(Translatable request) {
    final var translator = translators.get(request.getClass());
    if (translator != null) {
      return translator.apply(request);
    }
    return NotificationTranslator.translate(request);
  }
}
