package de.vsy.client;

import static de.vsy.shared_utility.standard_value.StandardIdProvider.STANDARD_CLIENT_ID;

import de.vsy.client.controlling.ChatClientController;
import de.vsy.shared_module.packet_creation.ContentIdentificationProviderImpl;
import de.vsy.shared_module.packet_creation.PacketCompiler;
import de.vsy.shared_transmission.packet.property.communicator.CommunicationEndpoint;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Handles Client Connection. Processes client requests and transfers requests into server readable
 * requests.
 */
public class ChatClient {

  private static final int[] SERVER_PORTS = {7370, 7371};
  private static final String SERVER_ADDRESS = "127.0.0.1";
  private static final Logger LOGGER = LogManager.getLogger();
  private ChatClientController clientController;
  private ExecutorService clientHandler;

  public ChatClient() {
    this.clientController = new ChatClientController(SERVER_ADDRESS, SERVER_PORTS);
  }

  /**
   * The main method.
   *
   * @param args the arguments
   */
  public static void main(final String[] args) {
    final var client = new ChatClient();
    setupPacketCompiler();
    client.handleClient();
  }

  private static void setupPacketCompiler() {
    PacketCompiler.addOriginatorEntityProvider(
        () -> CommunicationEndpoint.getClientEntity(STANDARD_CLIENT_ID));
    PacketCompiler.addContentIdentificationProvider(new ContentIdentificationProviderImpl());
  }

  public void handleClient() {
    LOGGER.info("Client started.");

    while (this.clientController.clientNotTerminated()) {
      this.clientHandler = Executors.newSingleThreadExecutor();
      this.clientHandler.execute(this.clientController);
    }

    try {
      final var handlerShutdown = this.clientHandler.awaitTermination(500, TimeUnit.MILLISECONDS);

      if (!handlerShutdown) {
        LOGGER.error("ClientHandler thread could not be shutdown. Client will be terminated.");
        this.clientController.closeApplication();
      }
    } catch (InterruptedException ie) {
      LOGGER.error("Interrupted while waiting for clientHandler thread to terminate.");
    }
  }
}
