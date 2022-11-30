package de.vsy.client.packet_processing;

import de.vsy.shared_module.packet_management.PacketTransmissionCache;
import de.vsy.shared_transmission.packet.content.PacketContent;
import de.vsy.shared_transmission.packet.property.communicator.CommunicationEndpoint;

public class ResultingPacketContentHandler {

  private final PacketTransmissionCache packetCache;
  private final ResultingPacketCreator standardResultCreator;

  public ResultingPacketContentHandler(final PacketTransmissionCache packetCache,
      final ResultingPacketCreator standardResultCreator) {
    this.packetCache = packetCache;
    this.standardResultCreator = standardResultCreator;
  }

  public void addRequest(PacketContent content) {
    final var request = this.standardResultCreator.createRequest(content);
    this.packetCache.addPacket(request);
  }

  public void addRequest(PacketContent content, CommunicationEndpoint recipient) {
    final var request = this.standardResultCreator.createRequest(content, recipient);
    this.packetCache.addPacket(request);
  }

  public void setError(final PacketContent errorContent) {
    final var response = this.standardResultCreator.createResponse(errorContent);
    this.packetCache.putError(response);
  }

}
