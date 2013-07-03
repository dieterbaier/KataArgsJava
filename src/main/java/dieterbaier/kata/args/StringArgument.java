package dieterbaier.kata.args;

class StringArgument extends Argument<String> {

  StringArgument(final String defaultValue, final String defaultValueForSetFlag) {
    super(defaultValue, defaultValueForSetFlag);
  }

  @Override
  protected String convertToTypedValue(final String value) {
    return value;
  }

}
