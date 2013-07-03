package dieterbaier.kata.args;

import java.util.List;

class BooleanArgument extends Argument<Boolean> {

  BooleanArgument(final char key, final Boolean defaultValue) {
    super(key, defaultValue);
  }

  @Override
  Boolean convertToTypedValue(final String value) {
    final String valueWithLowerCase = value.toLowerCase();
    if (value == null || !"true".equals(valueWithLowerCase) && !"false".equals(valueWithLowerCase))
      throw new IllegalArgumentException("Invalid argument for flag " + getKey());
    return Boolean.parseBoolean(value);
  }

  @Override
  void setDefaultValueForGivenFlag() {
    setValue("true");
  }

  @Override
  Boolean[] toArray(final List<Boolean> typedValues) {
    return typedValues.toArray(new Boolean[typedValues.size()]);
  }

}
