package dieterbaier.kata.args;

class BooleanArgument extends Argument<Boolean> {

  BooleanArgument(final boolean defaultValue, final boolean defaultValueForSetFlag) {
    super(defaultValue, defaultValueForSetFlag);
  }

  @Override
  protected Boolean convertToTypedValue(final String value) {
    final String lowerCaseValue = value.toLowerCase();
    if (!"true".equals(lowerCaseValue) && !"false".equals(lowerCaseValue))
      throw new IllegalArgumentException();
    return Boolean.parseBoolean(value);
  }

}
