package de.vsy.client.packet_exception_processing;

import de.vsy.shared_module.packet_exception.PacketHandlingException;
import de.vsy.shared_transmission.packet.Packet;

public class PacketHandlingExceptionProcessor {

  public Packet processException(PacketHandlingException phe, Packet affectedPacket) {
    throw new UnsupportedOperationException("Es werden noch keine Fehlermeldungen verarbeitet.");
  }
}
