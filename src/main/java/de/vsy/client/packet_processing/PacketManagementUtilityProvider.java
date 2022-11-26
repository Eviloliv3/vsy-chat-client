package de.vsy.client.packet_processing;

import de.vsy.shared_module.packet_management.PacketTransmissionCache;

public class PacketManagementUtilityProvider implements ResultingContentHandlingProvider {

  private final PacketTransmissionCache packetCache;
  private final ResultingPacketCreator packetCreator;
  private final ResultingPacketContentHandler contentHandler;

  public PacketManagementUtilityProvider() {
    this.packetCache = new PacketTransmissionCache();
    this.packetCreator = new ResultingPacketCreator();
    this.contentHandler = new ResultingPacketContentHandler(packetCache, packetCreator);
  }

  public PacketTransmissionCache getPacketTransmissionCache() {
    return this.packetCache;
  }

  public ResultingPacketCreator getResultingPacketCreator() {
    return this.packetCreator;
  }

  @Override
  public ResultingPacketContentHandler getResultingPacketContentHandler() {
    return this.contentHandler;
  }
}
