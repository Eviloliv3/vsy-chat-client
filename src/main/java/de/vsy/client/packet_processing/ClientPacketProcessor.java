package de.vsy.client.packet_processing;

import de.vsy.shared_module.packet_exception.PacketProcessingException;
import de.vsy.shared_module.packet_exception.PacketValidationException;
import de.vsy.shared_module.packet_processing.ContentProcessor;
import de.vsy.shared_module.packet_processing.PacketProcessor;
import de.vsy.shared_module.packet_processing.ProcessingCondition;
import de.vsy.shared_module.packet_validation.content_validation.PacketContentValidator;
import de.vsy.shared_transmission.packet.Packet;
import de.vsy.shared_transmission.packet.content.PacketContent;

public class ClientPacketProcessor<RequestContent extends PacketContent>
    implements PacketProcessor {

  protected final ProcessingCondition processingCondition;
  protected final PacketContentValidator<RequestContent> contentValidator;
  protected final ContentProcessor<RequestContent> contentProcessor;

  public ClientPacketProcessor(
      ProcessingCondition processingCondition,
      PacketContentValidator<RequestContent> contentValidator,
      ContentProcessor<RequestContent> contentProcessor) {
    this.processingCondition = processingCondition;
    this.contentValidator = contentValidator;
    this.contentProcessor = contentProcessor;
  }

  @Override
  public void processPacket(Packet input)
      throws PacketProcessingException, PacketValidationException {

    if (processingCondition.checkCondition()) {
      final var content = input.getPacketContent();
      final var validatedContent = this.contentValidator.castAndValidateContent(content);
      this.contentProcessor.processContent(validatedContent);
    } else {
      throw new PacketProcessingException(processingCondition.getErrorMessage());
    }
  }
}
