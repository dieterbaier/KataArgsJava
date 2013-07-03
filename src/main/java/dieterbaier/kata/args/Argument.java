package dieterbaier.kata.args;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

abstract class Argument<T> {

  /**
   * Can be used, in case the value for the argument was not set
   */
  protected final T  defaultValue;

  /**
   * In case this argument was given by any argument-list, it's values will be stored here
   */
  protected T[]      value = null;

  private final char key;

  Argument(final char key, final T defaultValue) {
    this.key = key;
    this.defaultValue = defaultValue;
  }

  abstract T convertToTypedValue(final String value);

  char getKey() {
    return key;
  }

  String getValue() {
    if (value == null)
      return defaultValue.toString();
    /*
     * Arrays.toString() puts a opening and a closing bracket around the string and puts a blank behind every comma;
     * Since we want to return the original value, we remove all these unwanted signs or replace them with valid ones
     */
    return Arrays.toString(value).replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(", ", ",");
  }

  /**
   * In case you provided more than one value for a argument but you don't want to get the orinal string you can use
   * this method as conveniant method for iterating over all the orinal given values
   */
  String[] getValues() {
    return getValue().split(",");
  }

  /**
   * In case there was a flag given in the argument list, but the argument for it is missing, you can set a
   * default-value for this situation.<br>
   * <br>
   * Note, that this default-value doesn't need to be the same as the initiating default value of this argument<br>
   * <br>
   * Example: you defined a logging-flag for the valid argument; if the flag is not set it should have a value 'false';
   * if it is set, it should take the given argument. But as this flag is kind of a boolean-flag, the argument is not
   * mandatory, so if the argument is missing it should have the value 'true'
   */
  abstract void setDefaultValueForGivenFlag();

  /**
   * @param value can be a single or multiple-value; if it is a multiple-value, you have to seperate the values with a
   *          comma
   */
  void setValue(final String value) {
    /*
     * The value can be a comma separated list, indicating, that there are more than one values to store;
     */
    final String[] values = value.split(",");
    final List<T> typedValues = new ArrayList<>(values.length);
    for (final String v : values)
      typedValues.add(convertToTypedValue(v));
    this.value = toArray(typedValues);
  }

  abstract T[] toArray(List<T> typedValues);

}
