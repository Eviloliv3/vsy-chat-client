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
          "Request will not be processed. You already authenticated yourself.");
      case AUTHENTICATED -> new HandlerProcessingCondition<>(
          dataModel::isClientLoggedIn,
          TRUE,
          "Request will not be processed. You already authenticated yourself.");
      case ACTIVE_MESSENGER -> new HandlerProcessingCondition<>(
          dataModel::isClientLoggedIn, Boolean.TRUE,
          "Request will not be processed. You are registered as active messenger.");
      case NOT_ACTIVE_MESSENGER -> new HandlerProcessingCondition<>(
          dataModel::isClientLoggedIn, Boolean.FALSE,
          "Request will not be processed. You are not registered as active messenger.");
    };
  }
}
