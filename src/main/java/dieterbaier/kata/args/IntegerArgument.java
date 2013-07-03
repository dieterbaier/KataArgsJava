package dieterbaier.kata.args;

import java.util.List;

class IntegerArgument extends Argument<Integer> {

  IntegerArgument(final char key, final Integer defaultValue) {
    super(key, defaultValue);
  }

  @Override
  Integer convertToTypedValue(final String value) {
    try {
      return Integer.parseInt(value);
    }
    catch (final NumberFormatException e) {
      throw new IllegalArgumentException("Invalid argument for type " + getKey(), e);
    }
  }

  @Override
  void setDefaultValueForGivenFlag() {
    setValue("0");
  }

  @Override
  Integer[] toArray(final List<Integer> typedValues) {
    return typedValues.toArray(new Integer[typedValues.size()]);
  }

}
