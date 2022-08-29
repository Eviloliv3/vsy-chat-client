/*
 *
 */
package de.vsy.chat.client.connection_handling;

import org.apache.logging.log4j.Logger;

import de.vsy.chat.module.packet_management.ThreadPacketBufferManager;
import de.vsy.chat.module.packet_transmission.ConnectionThreadControl;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.logging.log4j.LogManager;

/** Provides a simple interface for server connection manipulating actions. */
public class ConnectionManager {

  private static final ThreadLocalRandom RANDOM_NUMBER_GENERATOR;
  private final Logger logger;
  private final ThreadPacketBufferManager packetBuffers;
  private final String serverAddress;
  private final int[] serverPorts;
  private ConnectionThreadControl connectionControl;
  private Socket connectionSocket;

  static {
    RANDOM_NUMBER_GENERATOR = ThreadLocalRandom.current();
  }

  /**
   * Instantiates a new connection manager.
   *
   * @param serverAddress the server address
   * @param serverPorts the server ports
   * @param PacketBuffers the Packetbuffers
   */
  public ConnectionManager(
      final String serverAddress,
      final int[] serverPorts,
      final ThreadPacketBufferManager packetBuffers) {

    if (serverAddress == null) {
      throw new IllegalArgumentException("Ungueltige Server Adresse: null.");
    }

    if (serverPorts.length <= 0) {
      throw new IllegalArgumentException("Mindestens ein Serverport muss angegeben werden.");
    }

    if (packetBuffers == null) {
      throw new IllegalArgumentException("Keine Verbindung ohne ThreadPacketBuffer moeglich.");
    }
    this.logger = LogManager.getLogger();
    this.serverAddress = serverAddress;
    this.serverPorts = serverPorts;
    this.packetBuffers = packetBuffers;
  }

  public void closeConnection() {

    if (this.connectionControl != null) {
      this.connectionControl.closeConnection();
    } else {
      this.logger.info("Es musste keine Verbindung getrennt werden.");
    }
  }

  /**
   * Initiate connection.
   *
   * @return true, if successful
   */
  public boolean initiateConnection() {
    this.logger.info("\nVerbindungsaufbau gestartet.");

    var reconnectTries = 3;

    if (!getConnectionState()) {

      while (reconnectTries > 0) {

        if (setupConnection()) {
          this.logger.info("\nVerbindungsaufbau erfolgreich.");
          return true;
        }
        reconnectTries--;
      }
    }
    this.logger.info("\nVerbindungsaufbau fehlgeschlagen.");
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
          this.logger.info("Warte auf ReadThread-/WriteThread-Startbestaetigung.");
          Thread.yield();
        } while (!this.connectionControl.connectionIsLive() && Thread.currentThread().isInterrupted());
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

          this.logger.info("Verbunden mit Server: {}", portToTest);
        } catch (final UnknownHostException uhe) {
          final var errorMessage =
              "Host / Port: "
                  + this.serverAddress
                  + "/"
                  + portToTest
                  + " - Kombination unbekannt.\nFehlernachricht: "
                  + uhe.getMessage();
          this.logger.error(errorMessage);
        } catch (final IOException ioe) {
          final var errorMessage =
              "Host / Port: "
                  + this.serverAddress
                  + "/"
                  + portToTest
                  + " - Kein Verbindung wird angeboten.\nFehlernachricht: "
                  + ioe.getMessage();
          this.logger.error(errorMessage);
        }
        testedPorts.add(portToTest);
      } else {
        this.logger.error("Verbindung wurde bereits getestet");
      }
    }
    return socket;
  }

  /** Setup connection control. */
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
