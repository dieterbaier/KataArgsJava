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

  public ArgumentFlag(final char name) {
    this.name = name;
    setDefaultValue();
  }

  private void setDefaultValue() {
    this.valueType = Boolean.class;
    this.value = false;
  }

  public void withIntegerValue() {
    this.valueType = Integer.class;
    this.value = 0;
  }

  public void withStringValue() {
    this.valueType = String.class;
    this.value = "";
  }

  public boolean isValid(String potentialFlag) {
    if (isPotentialFlag(potentialFlag))
      return potentialFlag.charAt(1) == name;
    else
      return false;
  }

  public static boolean isPotentialFlag(String potentialFlag) {
    return potentialFlag != null && potentialFlag.length() == 2;
  }

  public Object value() {
    return value;
  }

  public boolean needsValue() {
    return !(valueType == Boolean.class);
  }

  public void setValue(final String value) throws IllegalArgumentException {
    if (value == null)
      throw new IllegalArgumentException("Illegal value null for flag " + name);
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

  @Override
  public int hashCode() {
    return "ArgumentFlag".hashCode() + name + value.hashCode() + valueType.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if (this == obj)
      return true;
    return this.hashCode() == obj.hashCode();
  }

  public Character name() {
    return name;
  }

  public void setValue(boolean b) {
    value = b;
  }

}
