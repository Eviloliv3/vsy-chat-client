package de.vsy.chat.client.packet_processing;

import java.util.function.Supplier;

import de.vsy.chat.module.packet_processing.ProcessingCondition;

public class HandlerProcessingCondition<T> implements ProcessingCondition {

  private final Supplier<T> conditionSupplier;
  private final T toCompare;
  private final String errorMessage;

  public HandlerProcessingCondition(Supplier<T> supplier, T toCompare, String errorMessage) {
    this.conditionSupplier = supplier;
    this.toCompare = toCompare;
    this.errorMessage = errorMessage;
  }

  @Override
  public boolean checkCondition() {
    return this.conditionSupplier.get().equals(toCompare);
  }

  @Override
  public String getErrorMessage() {
    return errorMessage;
  }
}
