package de.vsy.client.packet_processing;

import de.vsy.client.data_model.notification.SimpleInformation;
import de.vsy.client.packet_processing.content_processing.SimpleInformationTranslator;
import de.vsy.shared_module.packet_content_translation.NotificationTranslator;

public class ClientNotificationTranslator extends NotificationTranslator {
  static{
    translators.put(SimpleInformation.class, SimpleInformationTranslator::translate);
  }
    protected ClientNotificationTranslator(){}
}
