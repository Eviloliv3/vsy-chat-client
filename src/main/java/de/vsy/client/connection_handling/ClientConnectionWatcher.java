package de.vsy.client.connection_handling;

import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

public class ClientConnectionWatcher extends TimerTask {

  private final ServerConnectionController connectionManager;
  private final CountDownLatch connectionWaiter;

  public ClientConnectionWatcher(ServerConnectionController connectionManager,
      CountDownLatch connectionWaiter) {
    this.connectionManager = connectionManager;
    this.connectionWaiter = connectionWaiter;
  }

  @Override
  public void run() {
    if (!this.connectionManager.getConnectionState()) {
      this.connectionWaiter.countDown();
    }
  }
}
