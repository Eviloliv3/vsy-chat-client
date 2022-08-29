/*
 *
 */
package de.vsy.chat.client.chat_client;

import de.vsy.chat.client.controlling.ChatClientController;
import de.vsy.chat.client.gui.chatter_main_model.ClientChatGUI;
import de.vsy.chat.module.packet_creation.ContentIdentificationProviderImpl;
import de.vsy.chat.module.packet_creation.PacketCompiler;
import de.vsy.chat.transmission.packet.property.communicator.CommunicationEndpoint;

import static de.vsy.chat.utility.standard_value.StandardIdProvider.STANDARD_CLIENT_ID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Handles Client Connection. Processes client requests and transfers requests into server readable
 * requests.
 */
public class ChatClient {

  private static final String SERVER_ADDRESS = "127.0.0.1";
  private final ChatClientController clientController;
  private final ClientChatGUI clientGui;
  private final Logger logger = LogManager.getLogger();
  private final int[] serverPorts = {7370, 7371};

  public ChatClient() {
    this.clientGui = new ClientChatGUI();
    this.clientController =
        new ChatClientController(SERVER_ADDRESS, this.serverPorts, this.clientGui);
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
    this.logger.info("\nClient gestartet.");

    try {
      this.clientController.startController();
    } catch (final InterruptedException ie) {
      Thread.currentThread().interrupt();
      this.logger.error("Klient unterbrochen.");
    }
  }
}
