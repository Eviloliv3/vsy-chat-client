/*
 *
 */
package de.vsy.chat.client.controlling.packet_creation;

import static de.vsy.chat.utility.standard_value.StandardIdProvider.STANDARD_CLIENT_ID;

import de.vsy.chat.module.packet_creation.PacketCompiler;
import de.vsy.chat.module.packet_management.OutputBuffer;
import de.vsy.chat.transmission.packet.Packet;
import de.vsy.chat.transmission.packet.content.PacketContent;
import de.vsy.chat.transmission.packet.property.communicator.CommunicationEndpoint;

/** Creation tool for simple Packetcreation and transmission. */
public class RequestPacketCreator {

  private final OutputBuffer outputHandle;

  /**
   * Instantiates a new request creator.
   *
   * @param outputHandle the output handle
   */
  public RequestPacketCreator(final OutputBuffer outputHandle) {
    this.outputHandle = outputHandle;
  }

  /**
   * Request.
   *
   * @param Packetata the PacketdataManagement
   * @param receiverId the receiver id
   */
  public void request(final PacketContent Packetata, final int receiverId) {
    Packet request;

    if (receiverId == STANDARD_CLIENT_ID) {
      request =
          PacketCompiler.createRequest(
              CommunicationEndpoint.getClientEntity(receiverId), Packetata);
    } else {
      request =
          PacketCompiler.createRequest(
              CommunicationEndpoint.getServerEntity(receiverId), Packetata);
    }
    sendRequest(request);
  }

  /**
   * Send request.
   *
   * @param output the output
   */
  private void sendRequest(final Packet output) {
    this.outputHandle.appendPacket(output);
  }
}
