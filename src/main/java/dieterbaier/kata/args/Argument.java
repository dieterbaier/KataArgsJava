package dieterbaier.kata.args;

abstract class Argument<T> {
  private final T defaultValue;

  private T       value;

  private final T defaultValueForSetFlag;

  Argument(final T defaultValue, final T defaultValueForSetFlag) {
    this.defaultValue = defaultValue;
    this.defaultValueForSetFlag = defaultValueForSetFlag;
  }

  String getDefaultValue() {
    return defaultValue.toString();
  }

  String getDefaultValueForSetFlag() {
    return defaultValueForSetFlag.toString();
  }

  String getValue() {
    if (value == null)
      return getDefaultValue();
    return value.toString();
  }

  void setValue(final String value) {
    this.value = convertToTypedValue(value);
  }

  protected abstract T convertToTypedValue(final String value);

}
