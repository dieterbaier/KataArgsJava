package dieterbaier.kata.args;

import java.util.ArrayList;
import java.util.List;

public abstract class Argument<T> {
  private final T       defaultValue;

  private final List<T> value = new ArrayList<>(0);

  private final T       defaultValueForSetFlag;

  public Argument(final T defaultValue, final T defaultValueForSetFlag) {
    this.defaultValue = defaultValue;
    this.defaultValueForSetFlag = defaultValueForSetFlag;
  }

  public synchronized T[] getValue() {
    if (value.size() <= 0)
      value.add(defaultValue);
    return value.toArray(newTypedArray(value.size()));
  }

  String getDefaultValueForSetFlag() {
    return defaultValueForSetFlag.toString();
  }

  synchronized void setValue(final String value) {
    this.value.add(convertToTypedValue(value));
  }

  protected abstract T convertToTypedValue(final String value);

  protected abstract T[] newTypedArray(final int size);

}
