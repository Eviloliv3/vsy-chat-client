package de.vsy.client.packet_processing.content_processing;

import de.vsy.client.controlling.data_access_interfaces.AuthenticationDataModelAccess;
import de.vsy.client.data_model.notification.SimpleInformation;
import de.vsy.client.packet_processing.ResultingContentHandlingProvider;
import de.vsy.client.packet_processing.ResultingPacketContentHandler;
import de.vsy.shared_module.packet_exception.PacketProcessingException;
import de.vsy.shared_module.packet_processing.ContentProcessor;
import de.vsy.shared_transmission.packet.content.authentication.AccountDeletionResponseDTO;

public class AccountDeletionResponseProcessor implements
    ContentProcessor<AccountDeletionResponseDTO> {

  private final ResultingPacketContentHandler contentHandler;
  private final AuthenticationDataModelAccess dataModel;

  public AccountDeletionResponseProcessor(
      final AuthenticationDataModelAccess authenticationModelAccess,
      final ResultingContentHandlingProvider handlerProvider) {
    this.dataModel = authenticationModelAccess;
    this.contentHandler = handlerProvider.getResultingPacketContentHandler();
  }

  @Override
  public void processContent(AccountDeletionResponseDTO toProcess)
      throws PacketProcessingException {
    if (toProcess.getAccountDeleted()) {
      this.dataModel.addNotification(new SimpleInformation("Account deleted successfully."));
      this.dataModel.completeLogout();
    } else {
      this.dataModel.addNotification(
          new SimpleInformation("Account deletion failed. Please contact "
              + "the chatter support team."));
    }
    contentHandler.addRequest(toProcess);
  }
}
