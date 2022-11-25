package de.vsy.client.packet_processing;

import de.vsy.shared_module.packet_processing.ProcessingCondition;
import java.util.function.Supplier;

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
