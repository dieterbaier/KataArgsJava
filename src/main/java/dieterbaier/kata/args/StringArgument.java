package dieterbaier.kata.args;

import java.util.List;

class StringArgument extends Argument<String> {

  StringArgument(final char key, final String defaultValue) {
    super(key, defaultValue);
  }

  @Override
  String convertToTypedValue(final String value) {
    return value;
  }

  @Override
  void setDefaultValueForGivenFlag() {
    setValue("");
  }

  @Override
  String[] toArray(final List<String> typedValues) {
    return typedValues.toArray(new String[typedValues.size()]);
  }
}
