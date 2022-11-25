/*
 *
 */
package de.vsy.client.packet_processing;

import de.vsy.client.connection_handling.ServerConnectionController;
import de.vsy.client.packet_exception_processing.PacketHandlingExceptionProcessor;
import de.vsy.client.packet_processing.processor_provisioning.PacketProcessorManager;
import de.vsy.shared_module.packet_exception.PacketHandlingException;
import de.vsy.shared_module.packet_management.PacketBuffer;
import de.vsy.shared_module.packet_management.ThreadPacketBufferLabel;
import de.vsy.shared_module.packet_management.ThreadPacketBufferManager;
import de.vsy.shared_module.packet_processing.PacketProcessor;
import de.vsy.shared_module.packet_validation.PacketCheck;
import de.vsy.shared_transmission.packet.Packet;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Delegates the processing of incoming Packet and outgoing client requests.
 */
public class PacketProcessingService implements Runnable {

  private final Logger LOGGER = LogManager.getLogger();
  private final PacketHandlingExceptionProcessor pheProcessor;
  private final PacketProcessorManager phf;
  private final ServerConnectionController connectionControl;
  private final PacketCheck validator;
  private final PacketBuffer inputBuffer;
  private final PacketBuffer outputBuffer;

  /**
   * Instantiates a new PacketProcessingService.
   *
   * @param packetBuffers     the Packetbuffers
   * @param phf               the phf
   * @param pheProcessor      the phe handler
   * @param validator         the validator
   * @param connectionControl the connection control
   */
  //TODO ueberarbeiten?
  public PacketProcessingService(
      final ThreadPacketBufferManager packetBuffers,
      final PacketProcessorManager phf,
      final PacketHandlingExceptionProcessor pheProcessor,
      final PacketCheck validator,
      final ServerConnectionController connectionControl) {

    this.phf = phf;
    this.pheProcessor = pheProcessor;
    this.validator = validator;
    this.connectionControl = connectionControl;
    this.inputBuffer = packetBuffers.getPacketBuffer(ThreadPacketBufferLabel.HANDLER_BOUND);
    this.outputBuffer = packetBuffers.getPacketBuffer(ThreadPacketBufferLabel.OUTSIDE_BOUND);
  }

  @Override
  public void run() {
    LOGGER.info("Service gestartet.");

    do {
      processInput();
    } while (this.connectionControl.getConnectionState() && Thread.currentThread().isInterrupted());
    LOGGER.info("Service beendet.");
  }

  private void processInput() {
    Packet inputPacket;

    try {
      inputPacket = this.inputBuffer.getPacket();
    } catch (InterruptedException ie) {
      Thread.currentThread().interrupt();
      LOGGER.error("Beim Holen des naechsten Pakets unterbrochen.");
      inputPacket = null;
    }

    if (inputPacket != null) {
      Packet response = null;

      try {
        final var validatorString = this.validator.checkPacket(inputPacket);

        if (validatorString.isEmpty()) {
          final var packetProcessor = preparePacketProcessor(inputPacket);

          if (packetProcessor.isPresent()) {
            response = packetProcessor.get().processPacket(inputPacket);
          } else {
            final var errorMessage = "Packet not processed: no processor found.";
            throw new PacketHandlingException(errorMessage);
          }
        } else {
          final var errorMessage = "Das zuletzt gelesene Packetkonnte nicht verifiziert werden. ";
          throw new PacketHandlingException(errorMessage + validatorString.get());
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
  private Optional<PacketProcessor> preparePacketProcessor(final Packet inputPacket) {
    final var identifier = inputPacket.getPacketProperties().getPacketIdentificationProvider();
    final var contentType = inputPacket.getPacketContent().getClass();
    return this.phf.getProcessor(identifier, contentType);
  }
}
