package de.vsy.chat.client.packet_exception_processing;

import de.vsy.chat.module.packet_exception.PacketHandlingException;
import de.vsy.chat.transmission.packet.Packet;

public class PacketHandlingExceptionProcessor {

  public Packet processException(PacketHandlingException phe, Packet affectedPacket) {
    throw new UnsupportedOperationException("Es werden noch keine Fehlermeldungen verarbeitet.");
  }
}
