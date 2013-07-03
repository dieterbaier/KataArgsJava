package dieterbaier.kata.args;

class BooleanArgument extends Argument<Boolean> {

  public BooleanArgument(final String key, final Boolean defaultValue) {
    super(key, defaultValue);
  }

  @Override
  public void setDefaultValueForGivenFlag() {
    value = true;
  }

  @Override
  public void setValue(final String value) {
    if (value == null || !"true".equals(value.toLowerCase()) && !"false".equals(value.toLowerCase()))
      throw new IllegalArgumentException("Value for flag " + getKey() + " is invalid");
    this.value = Boolean.parseBoolean(value);
  }

}
