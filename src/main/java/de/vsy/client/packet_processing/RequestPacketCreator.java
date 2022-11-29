package de.vsy.client.packet_processing;

import de.vsy.shared_module.packet_management.PacketBuffer;
import de.vsy.shared_module.packet_management.PacketDispatcher;
import de.vsy.shared_module.packet_management.PacketTransmissionCache;
import de.vsy.shared_transmission.packet.content.PacketContent;
import de.vsy.shared_transmission.packet.property.communicator.CommunicationEndpoint;

public class RequestPacketCreator {

  private final PacketDispatcher dispatcher;
  private final PacketTransmissionCache packetsToSend;
  private final ResultingPacketContentHandler contentHandler;

  public RequestPacketCreator(final PacketBuffer outputBuffer, final PacketTransmissionCache packetCache, final ResultingPacketContentHandler contentHandler){
    this.dispatcher = outputBuffer::appendPacket;
    this.packetsToSend = packetCache;
    this.contentHandler = contentHandler;
  }

  public void request(PacketContent content, CommunicationEndpoint recipient){
    this.contentHandler.addRequest(content, recipient);
    this.packetsToSend.transmitPackets(this.dispatcher);
  }
}
