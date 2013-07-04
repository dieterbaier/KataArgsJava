package dieterbaier.kata.args;

public abstract class Argument<T> {
  private final T defaultValue;

  private T       value;

  private final T defaultValueForSetFlag;

  public Argument(final T defaultValue, final T defaultValueForSetFlag) {
    this.defaultValue = defaultValue;
    this.defaultValueForSetFlag = defaultValueForSetFlag;
  }

  public T getValue() {
    if (value == null)
      return defaultValue;
    return value;
  }

  String getDefaultValueForSetFlag() {
    return defaultValueForSetFlag.toString();
  }

  String getValueAsString() {
    if (value == null)
      return defaultValue.toString();
    return value.toString();
  }

  void setValue(final String value) {
    this.value = convertToTypedValue(value);
  }

  protected abstract T convertToTypedValue(final String value);

}
