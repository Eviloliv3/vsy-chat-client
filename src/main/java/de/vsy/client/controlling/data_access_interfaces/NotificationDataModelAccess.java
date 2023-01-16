package de.vsy.client.controlling.data_access_interfaces;

/**
 * Provides NOTIFICATION related Packet handlers with appropriate data access.
 */
public interface NotificationDataModelAccess extends EssentialDataModelAccess {

  /**
   * Allows the notification Packet handlers to fully reset client.
   */
    void resetClient();
}
