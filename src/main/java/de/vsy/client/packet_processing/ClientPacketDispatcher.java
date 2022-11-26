package de.vsy.client.packet_processing;

import de.vsy.shared_module.packet_management.PacketBuffer;
import de.vsy.shared_module.packet_management.PacketDispatcher;
import de.vsy.shared_module.packet_management.ThreadPacketBufferLabel;
import de.vsy.shared_module.packet_management.ThreadPacketBufferManager;
import de.vsy.shared_transmission.packet.Packet;

public class ClientPacketDispatcher implements PacketDispatcher {

  private final PacketBuffer serverBoundBuffer;

  public ClientPacketDispatcher(final ThreadPacketBufferManager bufferManager) {
    this.serverBoundBuffer = bufferManager.getPacketBuffer(ThreadPacketBufferLabel.OUTSIDE_BOUND);
  }

  @Override
  public void dispatchPacket(Packet output) {

  }
}
