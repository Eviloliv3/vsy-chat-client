package de.vsy.client.packet_processing;

import de.vsy.shared_module.packet_creation.PacketCompiler;
import de.vsy.shared_transmission.packet.Packet;
import de.vsy.shared_transmission.packet.content.PacketContent;
import de.vsy.shared_transmission.packet.property.communicator.CommunicationEndpoint;

public class ResultingPacketCreator {

  protected Packet currentRequest;

  public void setCurrentPacket(Packet currentRequest) {
    this.currentRequest = currentRequest;
  }

  public Packet createRequest(PacketContent content) {
    final var recipient = this.currentRequest.getPacketProperties().getRecipient();
    return createRequest(content, recipient);
  }

  public Packet createRequest(PacketContent content, CommunicationEndpoint recipient) {
    return PacketCompiler.createRequest(recipient, content);
  }

  public Packet createResponse(PacketContent content) {
    return PacketCompiler.createResponse(content, this.currentRequest);
  }
}
