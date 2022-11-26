/*
 *
 */
package de.vsy.client.packet_processing.processor_provisioning;

import de.vsy.client.controlling.data_access_interfaces.AuthenticationDataModelAccess;
import de.vsy.client.packet_processing.ClientPacketProcessor;
import de.vsy.client.packet_processing.ResultingContentHandlingProvider;
import de.vsy.client.packet_processing.content_processing.LoginRequestProcessor;
import de.vsy.client.packet_processing.content_processing.LoginResponseProcessor;
import de.vsy.client.packet_processing.content_processing.LogoutRequestProcessor;
import de.vsy.client.packet_processing.content_processing.LogoutResponseProcessor;
import de.vsy.client.packet_processing.content_processing.NewAccountRequestProcessor;
import de.vsy.client.packet_processing.content_processing.ReconnectRequestProcessor;
import de.vsy.client.packet_processing.content_processing.ReconnectResponseProcessor;
import de.vsy.shared_module.packet_processing.PacketProcessor;
import de.vsy.shared_module.packet_processing.ProcessingConditionType;
import de.vsy.shared_module.packet_processing.processor_provision.ContentBasedProcessorFactory;
import de.vsy.shared_module.packet_validation.content_validation.authentication.LoginRequestValidator;
import de.vsy.shared_module.packet_validation.content_validation.authentication.LoginResponseValidator;
import de.vsy.shared_module.packet_validation.content_validation.authentication.LogoutRequestValidator;
import de.vsy.shared_module.packet_validation.content_validation.authentication.LogoutResponseValidator;
import de.vsy.shared_module.packet_validation.content_validation.authentication.NewAccountRequestValidator;
import de.vsy.shared_module.packet_validation.content_validation.authentication.ReconnectRequestValidator;
import de.vsy.shared_module.packet_validation.content_validation.authentication.ReconnectResponseValidator;
import de.vsy.shared_transmission.packet.content.PacketContent;
import de.vsy.shared_transmission.packet.content.authentication.AuthenticationContent;

/**
 * Factory for creating authentication category PacketHandlers.
 */
public class AuthenticationPacketProcessorFactory implements ContentBasedProcessorFactory {

  private final AuthenticationDataModelAccess dataManager;
  private final ResultingContentHandlingProvider handlerProvider;

  /**
   * Instantiates a new authentication handler.
   *
   * @param dataManager the dataManagement manager
   */
  public AuthenticationPacketProcessorFactory(final AuthenticationDataModelAccess dataManager,
      final ResultingContentHandlingProvider handlerProvider) {
    this.dataManager = dataManager;
    this.handlerProvider = handlerProvider;
  }

  public PacketProcessor createTypeProcessor(final Class<? extends PacketContent> contentType) {
    final var authenticationType = AuthenticationContent.valueOf(contentType.getSimpleName());

    switch (authenticationType) {
      case LoginRequestDTO:
        return new ClientPacketProcessor<>(
            ContentProcessingConditionProvider.getContentProcessingCondition(
                ProcessingConditionType.NOT_AUTHENTICATED, this.dataManager),
            new LoginRequestValidator(),
            new LoginRequestProcessor(this.dataManager, this.handlerProvider));
      case LoginResponseDTO:
        return new ClientPacketProcessor<>(
            ContentProcessingConditionProvider.getContentProcessingCondition(
                ProcessingConditionType.NOT_AUTHENTICATED, this.dataManager),
            new LoginResponseValidator(),
            new LoginResponseProcessor(this.dataManager));
      case LogoutRequestDTO:
        return new ClientPacketProcessor<>(
            ContentProcessingConditionProvider.getContentProcessingCondition(
                ProcessingConditionType.AUTHENTICATED, this.dataManager),
            new LogoutRequestValidator(),
            new LogoutRequestProcessor(this.dataManager, this.handlerProvider));
      case LogoutResponseDTO:
        return new ClientPacketProcessor<>(
            ContentProcessingConditionProvider.getContentProcessingCondition(
                ProcessingConditionType.AUTHENTICATED, this.dataManager),
            new LogoutResponseValidator(),
            new LogoutResponseProcessor(this.dataManager));
      case NewAccountRequestDTO:
        return new ClientPacketProcessor<>(
            ContentProcessingConditionProvider.getContentProcessingCondition(
                ProcessingConditionType.NOT_AUTHENTICATED, this.dataManager),
            new NewAccountRequestValidator(),
            new NewAccountRequestProcessor(this.dataManager, this.handlerProvider));
      case ReconnectRequestDTO:
        return new ClientPacketProcessor<>(
            ContentProcessingConditionProvider.getContentProcessingCondition(
                ProcessingConditionType.AUTHENTICATED, this.dataManager),
            new ReconnectRequestValidator(),
            new ReconnectRequestProcessor(this.dataManager, this.handlerProvider));
      case ReconnectResponseDTO:
        return new ClientPacketProcessor<>(
            ContentProcessingConditionProvider.getContentProcessingCondition(
                ProcessingConditionType.AUTHENTICATED, this.dataManager),
            new ReconnectResponseValidator(),
            new ReconnectResponseProcessor(this.dataManager));
      default:
        break;
    }
    return null;
  }
}
