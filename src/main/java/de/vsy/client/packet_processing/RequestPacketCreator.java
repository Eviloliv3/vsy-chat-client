/*
 *
 */
package de.vsy.client.packet_processing;

import static de.vsy.shared_utility.standard_value.StandardIdProvider.STANDARD_CLIENT_ID;

import de.vsy.shared_module.packet_creation.PacketCompiler;
import de.vsy.shared_module.packet_management.OutputBuffer;
import de.vsy.shared_transmission.packet.Packet;
import de.vsy.shared_transmission.packet.content.PacketContent;
import de.vsy.shared_transmission.packet.property.communicator.CommunicationEndpoint;

/**
 * Creation tool for simple packet creation and transmission.
 */
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
   * @param content     the content
   * @param recipientId the receiver id
   */
  public void request(final PacketContent content, final int recipientId) {
    Packet request;

    if (recipientId == STANDARD_CLIENT_ID) {
      request =
          PacketCompiler.createRequest(
              CommunicationEndpoint.getClientEntity(recipientId), content);
    } else {
      request =
          PacketCompiler.createRequest(
              CommunicationEndpoint.getServerEntity(recipientId), content);
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
