package dieterbaier.kata.args;

public class BooleanArgument extends Argument<Boolean> {

  public BooleanArgument(final boolean defaultValue, final boolean defaultValueForSetFlag) {
    super(defaultValue, defaultValueForSetFlag);
  }

  @Override
  protected Boolean convertToTypedValue(final String value) {
    final String lowerCaseValue = value.toLowerCase();
    if (!"true".equals(lowerCaseValue) && !"false".equals(lowerCaseValue))
      throw new IllegalArgumentException(value + " is not a valid argument for a boolean flag");
    return Boolean.parseBoolean(value);
  }

}
