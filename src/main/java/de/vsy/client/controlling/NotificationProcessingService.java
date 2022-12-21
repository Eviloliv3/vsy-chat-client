package de.vsy.client.controlling;

import de.vsy.client.data_model.ServerDataCache;
import de.vsy.client.gui.essential_graphical_unit.prompt.NotificationPanel;
import de.vsy.client.gui.essential_graphical_unit.prompt.RequestPanel;
import de.vsy.client.packet_processing.ClientNotificationTranslator;
import de.vsy.client.packet_processing.RequestPacketCreator;
import de.vsy.shared_transmission.packet.content.HumanInteractionRequest;
import de.vsy.shared_transmission.packet.content.Translatable;
import de.vsy.shared_transmission.packet.property.communicator.CommunicationEndpoint;
import javax.swing.JOptionPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NotificationProcessingService implements Runnable {

  private static final Logger LOGGER = LogManager.getLogger();
  private final ServerDataCache dataProvider;
  private final RequestPacketCreator requester;

  public NotificationProcessingService(final ServerDataCache dataProvider,
      final RequestPacketCreator requester) {
    this.dataProvider = dataProvider;
    this.requester = requester;
  }

  @Override
  public void run() {
    LOGGER.info("NotificationProcessingService started.");
    final var notificationProvider = dataProvider.getClientNotificationManager();
    Translatable currentNotification;

    do {
      currentNotification = notificationProvider.getNextNotification();

      if (currentNotification instanceof HumanInteractionRequest request) {
        handleContactRequest(request);
      } else if (currentNotification != null) {
        handleNotification(currentNotification);
      } else {
        LOGGER.error("No Translatable object.");
        break;
      }
    } while (!Thread.interrupted() || currentNotification != null);
    LOGGER.info("NotificationProcessingService terminated.");
  }

  private void handleContactRequest(HumanInteractionRequest request) {
    String translatedRequest = ClientNotificationTranslator.translate(request);
    var requestPanel = new RequestPanel(translatedRequest);
    String[] options = {"Accept", "Deny"};

    final var requestOption = JOptionPane.showOptionDialog(null, requestPanel, "Client request",
        JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
    final var decision = requestOption == JOptionPane.YES_OPTION;
    this.requester.request(
        request.setDecision(this.dataProvider.getCommunicatorData(),
            decision), CommunicationEndpoint.getClientEntity(request.getOriginatorId()));
  }

  private void handleNotification(Translatable notification) {
    final String message = ClientNotificationTranslator.translate(notification);

    if (message != null) {
      final var notificationPanel = new NotificationPanel(message);
      JOptionPane.showMessageDialog(null, notificationPanel, "Information",
          JOptionPane.INFORMATION_MESSAGE);
    } else {
      LOGGER.warn("Empty notification:\n{}", notification);
    }
  }
}
