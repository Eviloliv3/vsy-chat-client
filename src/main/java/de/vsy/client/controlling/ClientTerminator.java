package de.vsy.client.controlling;

@FunctionalInterface
public interface ClientTerminator {

  /**
   * Initiates client shutdown.
   */
  void closeApplication();
}
