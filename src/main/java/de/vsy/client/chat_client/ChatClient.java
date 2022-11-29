/*
 *
 */
package de.vsy.client.chat_client;

import static de.vsy.shared_utility.standard_value.StandardIdProvider.STANDARD_CLIENT_ID;

import de.vsy.client.controlling.ChatClientController;
import de.vsy.client.gui.chatter_main_model.ClientChatGUI;
import de.vsy.shared_module.packet_creation.ContentIdentificationProviderImpl;
import de.vsy.shared_module.packet_creation.PacketCompiler;
import de.vsy.shared_transmission.packet.property.communicator.CommunicationEndpoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Handles Client Connection. Processes client requests and transfers requests into server readable
 * requests.
 */
public class ChatClient {

  private static final String SERVER_ADDRESS = "127.0.0.1";
  private final ChatClientController clientController;
  private final Logger LOGGER = LogManager.getLogger();
  private final int[] serverPorts = {7370, 7371};

  public ChatClient() {
    this.clientController =
        new ChatClientController(SERVER_ADDRESS, this.serverPorts);
  }

  /**
   * The main method.
   *
   * @param args the arguments
   */
  public static void main(final String[] args) {
    final var client = new ChatClient();
    PacketCompiler.addOriginatorEntityProvider(
        () -> CommunicationEndpoint.getClientEntity(STANDARD_CLIENT_ID));
    PacketCompiler.addContentIdentificator(new ContentIdentificationProviderImpl());
    client.start();
  }

  public void start() {
    LOGGER.info("Client gestartet.");

    try {
      this.clientController.startController();
    } catch (final InterruptedException ie) {
      Thread.currentThread().interrupt();
      LOGGER.error("Klient unterbrochen.");
    }
  }
}
