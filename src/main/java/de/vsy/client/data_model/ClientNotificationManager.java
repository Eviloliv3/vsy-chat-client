package de.vsy.client.data_model;

import de.vsy.shared_transmission.packet.content.Translatable;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientNotificationManager {

  private static final Logger LOGGER = LogManager.getLogger();
  private final Lock lock;
  private final BlockingDeque<Translatable> notifications;

  public ClientNotificationManager() {
    this.lock = new ReentrantLock();
    this.notifications = new LinkedBlockingDeque<>();
  }

  public void addNotification(Translatable notification) {
    this.lock.lock();
    try {
      if (!(this.notifications.contains(notification))) {
        this.notifications.add(notification);
      } else {
        LOGGER.trace("Already contained, request discarded: {}", notification);
      }
    } finally {
      this.lock.unlock();
    }
  }

  public void prependNotification(Translatable notification) {
    this.lock.lock();
    try {
      if (!(this.notifications.contains(notification))) {
        this.notifications.addFirst(notification);
      } else {
        LOGGER.trace("Already contained, request discarded: {}", notification);
      }
    } finally {
      this.lock.unlock();
    }
  }

  public Translatable getNextNotification() {

    try {
      return this.notifications.take();
    } catch (InterruptedException ie) {
      Thread.currentThread().interrupt();
      LOGGER.error("Interrupted while waiting for next notification.");
    }
    return null;
  }
}
