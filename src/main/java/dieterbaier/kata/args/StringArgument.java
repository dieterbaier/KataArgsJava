package dieterbaier.kata.args;

class StringArgument extends Argument<String> {

  public StringArgument(final String key, final String defaultValue) {
    super(key, defaultValue);
  }

  @Override
  public void setDefaultValueForGivenFlag() {
    setValue("");
  }

  @Override
  public void setValue(final String value) {
    this.value = value;
  }

}
