package dieterbaier.kata.args;

abstract class Argument<T> {

  protected T          value;

  private final String key;

  protected Argument(final String key, final T defaultValue) {
    value = defaultValue;
    this.key = key;
  }

  public String getKey() {
    return key;
  }

  public String getValue() {
    return value.toString();
  }

  public abstract void setDefaultValueForGivenFlag();

  public abstract void setValue(String value);

}
