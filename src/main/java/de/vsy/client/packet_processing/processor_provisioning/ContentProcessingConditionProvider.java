package de.vsy.client.packet_processing.processor_provisioning;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import de.vsy.client.controlling.data_access_interfaces.EssentialDataModelAccess;
import de.vsy.client.packet_processing.HandlerProcessingCondition;
import de.vsy.shared_module.packet_processing.ProcessingCondition;
import de.vsy.shared_module.packet_processing.ProcessingConditionType;

public class ContentProcessingConditionProvider {

  public static ProcessingCondition getContentProcessingCondition(
      ProcessingConditionType conditionType, EssentialDataModelAccess dataModel) {

    return switch (conditionType) {
      case NOT_AUTHENTICATED -> new HandlerProcessingCondition<>(
          dataModel::isClientLoggedIn,
          FALSE,
          "Anfrage nicht bearbeitet. Sie sind bereits authentifiziert.");
      case AUTHENTICATED -> new HandlerProcessingCondition<>(
          dataModel::isClientLoggedIn,
          TRUE,
          "Anfrage nicht bearbeitet. Sie sind noch nicht authentifiziert.");
        /* case ACTIVE_MESSENGER:
            createdConditionProvider = new HandlerProcessingCondition<>(
                    dataModel::isClientLoggedIn, Boolean.TRUE,
                    "Anfrage nicht bearbeitet. Sie sind als Messenger registriert.");
            break;
        case NOT_ACTIVE_MESSENGER:
            createdConditionProvider = new HandlerProcessingCondition<>(
                    dataModel::isClientLoggedIn, Boolean.FALSE,
                    "Anfrage nicht bearbeitet. Sie sind nicht als Messenger registriert.");
            break;
        */
      default -> null;
    };
  }
}
