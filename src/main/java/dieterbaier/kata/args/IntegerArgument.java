package dieterbaier.kata.args;

class IntegerArgument extends Argument<Integer> {

  IntegerArgument(final Integer defaultValue, final Integer defaultValueForSetFlag) {
    super(defaultValue, defaultValueForSetFlag);
  }

  @Override
  protected Integer convertToTypedValue(final String value) {
    try {
      return Integer.parseInt(value);
    }
    catch (final NumberFormatException e) {
      throw new IllegalArgumentException(e);
    }
  }

}
