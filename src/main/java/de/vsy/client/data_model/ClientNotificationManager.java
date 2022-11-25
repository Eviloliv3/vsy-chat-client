package de.vsy.client.data_model;

import de.vsy.shared_transmission.packet.content.Translatable;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientNotificationManager {
  private static final Logger LOGGER = LogManager.getLogger();
  private final ReadWriteLock lock;
  private final BlockingDeque<Translatable> notifications;

  public ClientNotificationManager(){
    this.lock = new ReentrantReadWriteLock();
    this.notifications = new LinkedBlockingDeque<>();
  }

  public
  void addNotification(Translatable notification){
    this.lock.writeLock().lock();
    try{
      if(!(this.notifications.contains(notification))) {
        this.notifications.add(notification);
      }else{
        LOGGER.trace("Already contained, request discarded: {}", notification);
      }
    }finally{
      this.lock.writeLock().unlock();
    }
  }

  public
  void prependNotification(Translatable notification){
    this.lock.writeLock().lock();
    try{
      if(!(this.notifications.contains(notification))) {
        this.notifications.addFirst(notification);
      }else{
        LOGGER.trace("Already contained, request discarded: {}", notification);
      }
    }finally{
      this.lock.writeLock().unlock();
    }
  }

  public
  Translatable getNextNotification() {
    this.lock.writeLock().lock();
    try{
      return this.notifications.take();
    }catch(InterruptedException ie) {
      Thread.currentThread().interrupt();
      LOGGER.error("Interrupted while waiting for next notification.");
    } finally{
      this.lock.writeLock().unlock();
    }
    return null;
  }
}
