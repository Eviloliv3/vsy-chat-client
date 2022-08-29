/*
 *
 */
package de.vsy.chat.client.packet_processing;

import de.vsy.chat.client.connection_handling.ConnectionManager;
import de.vsy.chat.client.packet_exception_processing.PacketHandlingExceptionProcessor;
import de.vsy.chat.client.packet_processing.processor_provisioning.PacketProcessorProvider;
import de.vsy.chat.module.packet_exception.PacketHandlingException;
import de.vsy.chat.module.packet_management.PacketBuffer;
import de.vsy.chat.module.packet_management.ThreadPacketBufferLabel;
import de.vsy.chat.module.packet_management.ThreadPacketBufferManager;
import de.vsy.chat.module.packet_processing.PacketProcessor;
import de.vsy.chat.module.packet_validation.PacketCheck;
import de.vsy.chat.transmission.packet.Packet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Delegates the processing of incoming Packet and outgoing client requests. */
public class PacketProcessingService implements Runnable {

  private final Logger logger;
  private final PacketHandlingExceptionProcessor pheProcessor;
  private final PacketProcessorProvider phf;
  private final ConnectionManager connectionControl;
  private final PacketCheck validator;
  private final PacketBuffer inputBuffer;
  private final PacketBuffer outputBuffer;

  /**
   * Instantiates a new Packetprocessing service.
   *
   * @param packetBuffers the Packetbuffers
   * @param phf the phf
   * @param pheProcessor the phe handler
   * @param validator the validator
   * @param connectionControl the connection control
   */
  public PacketProcessingService(
      final ThreadPacketBufferManager packetBuffers,
      final PacketProcessorProvider phf,
      final PacketHandlingExceptionProcessor pheProcessor,
      final PacketCheck validator,
      final ConnectionManager connectionControl) {
    this.logger = LogManager.getLogger();
    this.phf = phf;
    this.pheProcessor = pheProcessor;
    this.validator = validator;
    this.connectionControl = connectionControl;
    this.inputBuffer = packetBuffers.getPacketBuffer(ThreadPacketBufferLabel.HANDLER_BOUND);
    this.outputBuffer = packetBuffers.getPacketBuffer(ThreadPacketBufferLabel.OUTSIDE_BOUND);
  }

  @Override
  public void run() {
    this.logger.info("Service gestartet.");

    do {
      processInput();
      Thread.yield();
    } while (this.connectionControl.getConnectionState()&& Thread.currentThread().isInterrupted());
    this.logger.info("Service beendet.");
  }

  private void processInput() {
    Packet inputPacket;

    try {
      inputPacket = this.inputBuffer.getPacket();
    } catch (InterruptedException ie) {
      Thread.currentThread().interrupt();
      this.logger.error("Beim Holen des naechsten Pakets unterbrochen.");
      inputPacket = null;
    }

    if (inputPacket != null) {
      Packet response = null;

      try {
        final var validatorString = this.validator.checkPacket(inputPacket);

        if (validatorString == null) {
          final var packetProcessor = preparePacketProcessor(inputPacket);

          if (packetProcessor != null) {
            response = packetProcessor.processPacket(inputPacket);
          } else {
            final var errorMessage = "Das zuletzt gelesene Packetkonnte nicht verarbeitet werden.";
            final var errorCause = "Fuer diese Kombination existiert kein Prozessor.";
            throw new PacketHandlingException(errorMessage + errorCause);
          }
        } else {
          final var errorMessage = "Das zuletzt gelesene Packetkonnte nicht verifiziert werden.";
          throw new PacketHandlingException(errorMessage + validatorString);
        }
      } catch (final PacketHandlingException phe) {
        this.pheProcessor.processException(phe, inputPacket);
      }

      if (response != null) {
        this.outputBuffer.appendPacket(response);
      }
    }
  }

  /**
   * Prepare PacketHandler.
   *
   * @param inputPacket the input
   * @return the PacketHandler
   */
  private PacketProcessor preparePacketProcessor(final Packet inputPacket) {
    final var identifier = inputPacket.getPacketProperties().getContentIdentifier();
    final var contentType = inputPacket.getPacketContent().getClass();
    return this.phf.getProcessor(identifier, contentType);
  }
}
