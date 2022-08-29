package de.vsy.chat.client.packet_processing.processor_provisioning;

import de.vsy.chat.client.controlling.data_access_interfaces.EssentialDataModelAccess;
import de.vsy.chat.client.packet_processing.HandlerProcessingCondition;
import de.vsy.chat.module.packet_processing.ProcessingCondition;
import de.vsy.chat.module.packet_processing.ProcessingConditionType;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class ContentProcessingConditionProvider {

  public static ProcessingCondition getContentProcessingCondition(
      ProcessingConditionType conditionType, EssentialDataModelAccess dataModel) {
    ProcessingCondition createdConditionProvider;

    switch (conditionType) {
      case NOT_AUTHENTICATED:
        createdConditionProvider =
            new HandlerProcessingCondition<>(
                dataModel::isClientLoggedIn,
                FALSE,
                "Anfrage nicht bearbeitet. Sie sind bereits authentifiziert.");
        break;
      case AUTHENTICATED:
        createdConditionProvider =
            new HandlerProcessingCondition<>(
                dataModel::isClientLoggedIn,
                TRUE,
                "Anfrage nicht bearbeitet. Sie sind noch nicht authentifiziert.");
        break;
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
      default:
        createdConditionProvider = null;
        break;
    }
    return createdConditionProvider;
  }
}
