package dieterbaier.kata.args;

public class IntegerArgument extends Argument<Integer> {

  public IntegerArgument(final Integer defaultValue, final Integer defaultValueForSetFlag) {
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

  @Override
  protected Integer[] newTypedArray(final int size) {
    return new Integer[size];
  }

}
