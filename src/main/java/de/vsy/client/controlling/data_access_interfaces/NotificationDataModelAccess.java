package de.vsy.client.controlling.data_access_interfaces;

public interface NotificationDataModelAccess extends EssentialDataModelAccess {

  /**
   * Allows the notification processing unit to fully reset client.
   */
    void resetClient();
}
