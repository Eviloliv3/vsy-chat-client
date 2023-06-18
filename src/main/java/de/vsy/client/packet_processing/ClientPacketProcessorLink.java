package de.vsy.client.packet_processing;

import de.vsy.client.packet_processing.processor_provisioning.PacketProcessorManager;
import de.vsy.shared_module.packet_exception.PacketProcessingException;
import de.vsy.shared_module.packet_exception.PacketValidationException;
import de.vsy.shared_module.packet_processing.AbstractPacketProcessorLink;
import de.vsy.shared_module.packet_processing.PacketProcessor;
import de.vsy.shared_transmission.packet.Packet;

public class ClientPacketProcessorLink extends AbstractPacketProcessorLink {

  private final PacketProcessorManager processingLogic;

  public ClientPacketProcessorLink(final PacketProcessorManager processingLogic) {
    super(null);
    this.processingLogic = processingLogic;
  }

  @Override
  public void processPacket(Packet input)
      throws PacketValidationException, PacketProcessingException {
    PacketProcessor processor;
    final var inputContent = input.getPacketContent();
    final var contentType = inputContent.getClass();
    final var identifier = input.getPacketProperties().getPacketIdentificationProvider();

    processor = this.processingLogic.getProcessor(identifier, contentType)
        .orElseThrow(() -> new PacketProcessingException(
            "No processor found for: " + input.getPacketProperties()
                .getPacketIdentificationProvider()));
    processor.processPacket(input);
  }
}
