package dieterbaier.kata.args;

class IntegerArgument extends Argument<Integer> {

  public IntegerArgument(final String key, final Integer defaultValue) {
    super(key, defaultValue);
  }

  @Override
  public void setDefaultValueForGivenFlag() {
    setValue("0");
  }

  @Override
  public void setValue(final String value) {
    try {
      this.value = Integer.parseInt(value);
    }
    catch (final NumberFormatException e) {
      throw new IllegalArgumentException("Value for flag " + getKey() + " is invalid", e);
    }
  }

}
