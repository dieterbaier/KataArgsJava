package dieterbaier.kata.args;

import java.util.HashMap;
import java.util.Map;

public class ArgumentParser {

  private final Map<Character, Argument<?>> arguments = new HashMap<Character, Argument<?>>();

  public ArgumentParser() {}

  public void addFlag(final char key, final boolean defaultValue, final boolean defaultValueForSetFlag) {
    arguments.put(key, new BooleanArgument(defaultValue, defaultValueForSetFlag));
  }

  public void addFlag(final char key, final Integer defaultValue, final Integer defaultValueForSetFlag) {
    arguments.put(key, new IntegerArgument(defaultValue, defaultValueForSetFlag));
  }

  public void addFlag(final char key, final String defaultValue, final String defaultValueForSetFlag) {
    arguments.put(key, new StringArgument(defaultValue, defaultValueForSetFlag));
  }

  public String get(final char key) {
    return arguments.get(key).getValue();
  }

  public void parse(final String[] args) {
    for (int i = 0; i < args.length; i++) {
      char key = ' ';
      final String currentArg = args[i];
      final String nextArgument = i < args.length - 1 ? args[i + 1] : null;
      if (isFlag(currentArg))
        key = currentArg.charAt(1);
      if (!arguments.keySet().contains(key))
        throw new IllegalArgumentException("Illegal flag" + key);
      final Argument<?> currentArgument = arguments.get(key);
      String value;
      if (nextArgument == null || isFlag(nextArgument))
        value = currentArgument.getDefaultValueForSetFlag();
      else {
        value = nextArgument;
        i++;
      }
      currentArgument.setValue(value);
    }
  }

  private boolean isFlag(final String argument) {
    return argument != null && argument.length() == 2 && argument.startsWith("-");
  }

}
