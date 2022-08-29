/*
 *
 */
package de.vsy.chat.client.packet_processing.processor_provisioning;

import static de.vsy.chat.client.packet_processing.processor_provisioning.ContentProcessingConditionProvider.getContentProcessingCondition;

import de.vsy.chat.client.controlling.data_access_interfaces.AuthenticationDataModelAccess;
import de.vsy.chat.client.packet_processing.ClientPacketProcessor;
import de.vsy.chat.client.packet_processing.content_processing.LoginRequestProcessor;
import de.vsy.chat.client.packet_processing.content_processing.LoginResponseProcessor;
import de.vsy.chat.client.packet_processing.content_processing.LogoutRequestProcessor;
import de.vsy.chat.client.packet_processing.content_processing.LogoutResponseProcessor;
import de.vsy.chat.client.packet_processing.content_processing.NewAccountRequestProcessor;
import de.vsy.chat.client.packet_processing.content_processing.ReconnectRequestProcessor;
import de.vsy.chat.client.packet_processing.content_processing.ReconnectResponseProcessor;
import de.vsy.chat.module.packet_processing.PacketProcessor;
import de.vsy.chat.module.packet_processing.ProcessingConditionType;
import de.vsy.chat.module.packet_processing.processor_provision.PacketCategoryProcessorFactory;
import de.vsy.chat.module.packet_validation.content_validation.authentication.LoginRequestValidator;
import de.vsy.chat.module.packet_validation.content_validation.authentication.LoginResponseValidator;
import de.vsy.chat.module.packet_validation.content_validation.authentication.LogoutRequestValidator;
import de.vsy.chat.module.packet_validation.content_validation.authentication.LogoutResponseValidator;
import de.vsy.chat.module.packet_validation.content_validation.authentication.NewAccountRequestValidator;
import de.vsy.chat.module.packet_validation.content_validation.authentication.ReconnectRequestValidator;
import de.vsy.chat.module.packet_validation.content_validation.authentication.ReconnectResponseValidator;
import de.vsy.chat.transmission.packet.content.PacketContent;
import de.vsy.chat.transmission.packet.content.authentication.AuthenticationContent;

/** Factory for creating authentication category PacketHandlers. */
public class AuthenticationPacketProcessorFactory implements PacketCategoryProcessorFactory {

  private final AuthenticationDataModelAccess dataManager;

  /**
   * Instantiates a new authentication handler.
   *
   * @param dataManager the dataManagement manager
   */
  public AuthenticationPacketProcessorFactory(final AuthenticationDataModelAccess dataManager) {
    this.dataManager = dataManager;
  }

  public PacketProcessor createTypeProcessor(final Class<? extends PacketContent> contentType) {
    final var contentType = AuthenticationContent.valueOf(contentType.getSimpleName());

    switch (contentType) {
      case LoginRequestDTO:
        return new ClientPacketProcessor<>(
            getContentProcessingCondition(
                ProcessingConditionType.NOT_AUTHENTICATED, this.dataManager),
            new LoginRequestValidator(),
            new LoginRequestProcessor(this.dataManager));
      case LoginResponseDTO:
        return new ClientPacketProcessor<>(
            getContentProcessingCondition(
                ProcessingConditionType.NOT_AUTHENTICATED, this.dataManager),
            new LoginResponseValidator(),
            new LoginResponseProcessor(this.dataManager));
      case LogoutRequestDTO:
        return new ClientPacketProcessor<>(
            getContentProcessingCondition(ProcessingConditionType.AUTHENTICATED, this.dataManager),
            new LogoutRequestValidator(),
            new LogoutRequestProcessor(this.dataManager));
      case LogoutResponseDTO:
        return new ClientPacketProcessor<>(
            getContentProcessingCondition(ProcessingConditionType.AUTHENTICATED, this.dataManager),
            new LogoutResponseValidator(),
            new LogoutResponseProcessor(this.dataManager));
      case NewAccountRequestDTO:
        return new ClientPacketProcessor<>(
            getContentProcessingCondition(
                ProcessingConditionType.NOT_AUTHENTICATED, this.dataManager),
            new NewAccountRequestValidator(),
            new NewAccountRequestProcessor(this.dataManager));
      case ReconnectRequestDTO:
        return new ClientPacketProcessor<>(
            getContentProcessingCondition(ProcessingConditionType.AUTHENTICATED, this.dataManager),
            new ReconnectRequestValidator(),
            new ReconnectRequestProcessor(this.dataManager));
      case ReconnectResponseDTO:
        return new ClientPacketProcessor<>(
            getContentProcessingCondition(ProcessingConditionType.AUTHENTICATED, this.dataManager),
            new ReconnectResponseValidator(),
            new ReconnectResponseProcessor(this.dataManager));
      default:
        break;
    }
    return null;
  }
}
