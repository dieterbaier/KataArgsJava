package dieterbaier.kata.args;

public class StringArgument extends Argument<String> {

  public StringArgument(final String defaultValue, final String defaultValueForSetFlag) {
    super(defaultValue, defaultValueForSetFlag);
  }

  @Override
  protected String convertToTypedValue(final String value) {
    return value;
  }

  @Override
  protected String[] newTypedArray(final int size) {
    return new String[size];
  }

}
