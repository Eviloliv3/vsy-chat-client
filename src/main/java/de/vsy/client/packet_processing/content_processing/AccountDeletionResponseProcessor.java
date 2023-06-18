package de.vsy.client.packet_processing.content_processing;

import de.vsy.client.controlling.data_access_interfaces.AuthenticationDataModelAccess;
import de.vsy.client.packet_processing.ResultingContentHandlingProvider;
import de.vsy.client.packet_processing.ResultingPacketContentHandler;
import de.vsy.shared_module.packet_processing.ContentProcessor;
import de.vsy.shared_transmission.packet.content.authentication.AccountDeletionResponseDTO;
import de.vsy.shared_transmission.packet.content.notification.SimpleInformationDTO;

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
  public void processContent(AccountDeletionResponseDTO toProcess) {
    if (toProcess.getAccountDeleted()) {
      this.dataModel.completeLogout();
      this.dataModel.addNotification(new SimpleInformationDTO("Account deleted successfully."));
    } else {
      this.dataModel.addNotification(
          new SimpleInformationDTO("Account deletion failed. Please contact "
              + "the chatter support team."));
    }
  }
}
