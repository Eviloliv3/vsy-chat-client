package de.vsy.client.packet_processing;

import de.vsy.shared_module.packet_creation.PacketCompiler;
import de.vsy.shared_module.packet_exception.PacketHandlingException;
import de.vsy.shared_module.packet_processing.ContentProcessor;
import de.vsy.shared_module.packet_processing.PacketProcessor;
import de.vsy.shared_module.packet_processing.ProcessingCondition;
import de.vsy.shared_module.packet_validation.content_validation.PacketContentValidator;
import de.vsy.shared_transmission.packet.Packet;
import de.vsy.shared_transmission.packet.content.PacketContent;
import de.vsy.shared_transmission.packet.content.error.ErrorDTO;
import org.apache.logging.log4j.LogManager;

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
  public Packet processPacket(Packet input) {
    PacketContent content = null;
    String errorMessage = null;

    if (processingCondition.checkCondition()) {
      content = input.getPacketContent();
      try {
        final var validatedContent = this.contentValidator.castAndValidateContent(content);
        content = this.contentProcessor.processContent(validatedContent);
      } catch (PacketHandlingException phe) {
        errorMessage = phe.getMessage();
      }
    } else {
      errorMessage = processingCondition.getErrorMessage();
    }

    if (errorMessage != null) {
      content = new ErrorDTO(errorMessage, input);
    }
    return createResultingPacket(content, input);
  }

  protected Packet createResultingPacket(PacketContent responseContent, Packet requestPacket) {
    Packet resultingPacket = null;

    if (responseContent != null) {
      if (requestPacket.getPacketContent().getClass().isInstance(responseContent)) {
        LogManager.getLogger()
            .info(
                "request: {}; response: {}",
                requestPacket.getPacketContent().getClass(),
                responseContent.getClass());
        resultingPacket = requestPacket;
      } else {
        resultingPacket = PacketCompiler.createResponse(responseContent, requestPacket);
      }
    }
    return resultingPacket;
  }
}
