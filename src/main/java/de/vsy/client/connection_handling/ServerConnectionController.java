
package de.vsy.client.connection_handling;

import de.vsy.shared_module.packet_management.ThreadPacketBufferManager;
import de.vsy.shared_module.packet_transmission.ConnectionThreadControl;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Provides a simple interface for server connection manipulating actions.
 */
public class ServerConnectionController {

  private static final ThreadLocalRandom RANDOM_NUMBER_GENERATOR;
  private static final Logger LOGGER = LogManager.getLogger();

  static {
    RANDOM_NUMBER_GENERATOR = ThreadLocalRandom.current();
  }

  private final ThreadPacketBufferManager packetBuffers;
  private final String serverAddress;
  private final int[] serverPorts;
  private ConnectionThreadControl connectionControl;
  private Socket connectionSocket;

  /**
   * Instantiates a new connection manager.
   *
   * @param serverAddress the server address
   * @param serverPorts   the server ports
   * @param packetBuffers the packet buffer manager
   */
  public ServerConnectionController(
      final String serverAddress,
      final int[] serverPorts,
      final ThreadPacketBufferManager packetBuffers) {

    if (serverAddress == null) {
      throw new IllegalArgumentException("Invalid server address.");
    }

    if (serverPorts.length == 0) {
      throw new IllegalArgumentException("No server port defined.");
    }

    if (packetBuffers == null) {
      throw new IllegalArgumentException("No ThreadPacketBuffers.");
    }

    this.serverAddress = serverAddress;
    this.serverPorts = serverPorts;
    this.packetBuffers = packetBuffers;
  }

  public void closeConnection() {

    if (this.connectionControl != null) {
      this.connectionControl.closeConnection();
      LOGGER.info("Connection closed.");
    } else {
      LOGGER.info("No connection to be closed.");
    }
  }

  /**
   * Initiate connection.
   *
   * @return true, if successful
   */
  public boolean initiateConnection() {
    LOGGER.info("Connection initiated.");

    var reconnectTries = 3;

      if (!(getConnectionState())) {

      while (reconnectTries > 0) {

        if (setupConnection()) {
          LOGGER.info("Connection established.");
          return true;
        }
        reconnectTries--;
      }
    }
    LOGGER.error("Connection failed.");
    return false;
  }

  /**
   * Gets the connection state.
   *
   * @return the connection state
   */
  public boolean getConnectionState() {

    if (this.connectionControl != null) {
      return this.connectionControl.connectionIsLive();
    }
    return false;
  }

  /**
   * Setup connection.
   *
   * @return true, if successful
   */
  private boolean setupConnection() {
    final var connected = true;

    if ((this.connectionSocket = setupSocketConnection()) != null) {
      setupConnectionControl();

      if (this.connectionControl.initiateConnectionThreads()) {

        do {
          LOGGER.info("Waiting for connection thread initiation.");
          Thread.yield();
        } while (!(this.connectionControl.connectionIsLive()) && Thread.currentThread()
            .isInterrupted());
        return connected;
      }
    }
    return !connected;
  }

  /**
   * Setup socket connection.
   *
   * @return the socket
   */
  private Socket setupSocketConnection() {
    Socket socket = null;
    final var testedPorts = new ArrayList<Integer>(this.serverPorts.length);
    var index = -1;
    var portToTest = -1;

    while (socket == null && testedPorts.size() < this.serverPorts.length) {
      index = RANDOM_NUMBER_GENERATOR.nextInt(this.serverPorts.length);
      portToTest = this.serverPorts[index];

      if (!(testedPorts.contains(portToTest))) {

        try {
          socket = new Socket(this.serverAddress, portToTest);
          LOGGER.info("Connected to server: {}", portToTest);
        } catch (final UnknownHostException uhe) {
          LOGGER.error("{} occurred testing {}:{}", uhe.getClass().getSimpleName(),
              this.serverAddress, portToTest);
        } catch (final IOException ioe) {
          LOGGER.error("{} occurred connecting to {}:{}", ioe.getClass().getSimpleName(),
              this.serverAddress, portToTest);
        }
        testedPorts.add(portToTest);
      } else {
        LOGGER.error("Connection tested already.");
      }
    }
    return socket;
  }

  /**
   * Setup connection control.
   */
  private void setupConnectionControl() {
    if (this.connectionControl == null) {
      this.connectionControl =
          new ConnectionThreadControl(this.connectionSocket, this.packetBuffers);
    } else {
      final var cache = this.connectionControl.getPacketCache();
      this.connectionControl =
          new ConnectionThreadControl(this.connectionSocket, this.packetBuffers, cache, false);
    }
  }
}
