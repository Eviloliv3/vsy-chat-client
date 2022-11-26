/*
 *
 */
package de.vsy.client.packet_processing;

import de.vsy.client.connection_handling.ServerConnectionController;
import de.vsy.client.data_model.InputController;
import de.vsy.client.packet_processing.processor_provisioning.PacketProcessorManager;
import de.vsy.client.packet_processing.processor_provisioning.StandardProcessorFactoryProvider;
import de.vsy.shared_module.packet_exception.PacketHandlingException;
import de.vsy.shared_module.packet_management.PacketBuffer;
import de.vsy.shared_module.packet_management.PacketDispatcher;
import de.vsy.shared_module.packet_management.PacketTransmissionCache;
import de.vsy.shared_module.packet_management.ThreadPacketBufferLabel;
import de.vsy.shared_module.packet_management.ThreadPacketBufferManager;
import de.vsy.shared_module.packet_processing.PacketProcessor;
import de.vsy.shared_module.packet_processing.PacketSyntaxCheckLink;
import de.vsy.shared_module.packet_validation.SemanticPacketValidator;
import de.vsy.shared_module.packet_validation.SimplePacketChecker;
import de.vsy.shared_transmission.packet.Packet;
import de.vsy.shared_transmission.packet.content.error.ErrorDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Delegates the processing of incoming Packet and outgoing client requests.
 */
public class PacketProcessingService implements Runnable {

  private final Logger LOGGER = LogManager.getLogger();
  private final ServerConnectionController connectionControl;
  private final PacketBuffer inputBuffer;
  private final ResultingPacketCreator packetCreator;
  private final PacketTransmissionCache packetsToTransmit;
  private final ResultingPacketContentHandler contentHandler;
  private final PacketDispatcher dispatcher;
  private final PacketProcessor processor;

  /**
   * Instantiates a new PacketProcessingService.
   *
   * @param packetManagement  the packet management utility
   * @param dataController    the data input controller
   * @param packetBuffers     the packet buffer manager
   * @param connectionControl the connection control
   */
  public PacketProcessingService(final PacketManagementUtilityProvider packetManagement, final
  InputController dataController,
      final ThreadPacketBufferManager packetBuffers,
      final ServerConnectionController connectionControl) {

    this.packetCreator = packetManagement.getResultingPacketCreator();
    this.packetsToTransmit = packetManagement.getPacketTransmissionCache();
    this.contentHandler = packetManagement.getResultingPacketContentHandler();
    this.connectionControl = connectionControl;
    this.inputBuffer = packetBuffers.getPacketBuffer(ThreadPacketBufferLabel.HANDLER_BOUND);
    this.dispatcher = new ClientPacketDispatcher(packetBuffers);
    this.processor = createProcessor(dataController, packetManagement);
  }

  private PacketProcessor createProcessor(final InputController dataController,
      final PacketManagementUtilityProvider packetManagement) {
    final var processorManager = new PacketProcessorManager(dataController,
        new StandardProcessorFactoryProvider(), packetManagement);
    final var processorLink = new ClientPacketProcessorLink(processorManager);
    return new PacketSyntaxCheckLink(processorLink, new SimplePacketChecker(
        new SemanticPacketValidator()));

  }

  @Override
  public void run() {
    LOGGER.info("Service gestartet.");
    while (this.connectionControl.getConnectionState() && Thread.currentThread().isInterrupted()) {

      processInput();
    }
    LOGGER.info("Service beendet.");
  }

  private void processInput() {
    Packet input;

    try {
      input = this.inputBuffer.getPacket();
    } catch (InterruptedException ie) {
      Thread.currentThread().interrupt();
      LOGGER.error("Beim Holen des naechsten Pakets unterbrochen.");
      input = null;
    }

    if (input != null) {
      this.packetCreator.setCurrentPacket(input);

      try {
        this.processor.processPacket(input);
      } catch (final PacketHandlingException phe) {
        final var errorContent = new ErrorDTO(phe.getMessage(), input);
        this.contentHandler.setError(errorContent);
      }
      this.packetsToTransmit.transmitPackets(this.dispatcher);
    }
  }
}