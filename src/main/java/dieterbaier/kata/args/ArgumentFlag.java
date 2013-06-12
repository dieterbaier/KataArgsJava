package dieterbaier.kata.args;

/**
 * A flag has the format "-<any character>".
 * 
 * Note, that the name of the flag is only one character not a whole string. Therefore, the {@link #isValid(String)}
 * -method can check a given flag for validity with only checking the fixed length of 2 and the 1st character after the
 * minus-sign.
 */
public class ArgumentFlag {

  private char     name;

  private Class<?> valueType;

  private Object   value;

  public void withIntegerValue() {
    this.valueType = Integer.class;
    this.value = 0;
  }

  public void withStringValue() {
    this.valueType = String.class;
    this.value = "";
  }

  ArgumentFlag(final char name) {
    this.name = name;
    setDefaultValue();
  }

  boolean isValid(String potentialFlag) {
      return isPotentialFlag(potentialFlag) && potentialFlag.charAt(1) == name;
  }

  static boolean isPotentialFlag(String potentialFlag) {
    return potentialFlag != null && potentialFlag.length() == 2;
  }

  Object value() {
    return value;
  }

  boolean needsValue() {
    return !(valueType == Boolean.class);
  }

  void setValue(final String value) throws IllegalArgumentException {
    try {
      if (valueType == Integer.class) {
        this.value = Integer.parseInt(value);
      }
      else
        this.value = new String(value);
    }
    catch (Exception e) {
      throw new IllegalArgumentException("Illegal value " + value + " for flag " + name, e);
    }
  }

  Character name() {
    return name;
  }

  void setValue(boolean b) {
    value = b;
  }

  private void setDefaultValue() {
    this.valueType = Boolean.class;
    this.value = false;
  }

}
