package dieterbaier.kata.args;

import java.util.HashMap;
import java.util.Map;

public class ArgumentParser {

  private final Map<String, Argument<?>> arguments = new HashMap<>();

  public void addBooleanFlag(final String key, final boolean defaultValue) {
    arguments.put(key, new BooleanArgument(key, defaultValue));
  }

  public void addIntegerFlag(final String key, final int defaultValue) {
    arguments.put(key, new IntegerArgument(key, defaultValue));
  }

  public void addStringFlag(final String key, final String defaultValue) {
    arguments.put(key, new StringArgument(key, defaultValue));
  }

  public String getValue(final String key) {
    final Argument<?> argument = arguments.get(key);
    return argument.getValue();
  }

  public void parse(final String[] args) {
    for (int i = 0; i < args.length; i++) {
      final String nextArg = i < args.length - 1 ? args[i + 1] : "";
      if (isFlagWithArgument(args[i], nextArg))
        i++;
    }
  }

  private boolean isFlag(final String actArg) {
    return arguments.keySet().contains(actArg.substring(1));
  }

  private boolean isFlagWithArgument(final String currentArg, final String nextArg) {
    boolean isArgumentForFlagAvailable = false;

    if (!isFlag(currentArg))
      throw new IllegalArgumentException("Invalid Flag");

    final Argument<?> allreadySetArgument = arguments.get(currentArg.substring(1));
    if (!nextArg.isEmpty() && !isFlag(nextArg)) {
      allreadySetArgument.setValue(nextArg);
      isArgumentForFlagAvailable = true;
    }
    else
      allreadySetArgument.setDefaultValueForGivenFlag();

    return isArgumentForFlagAvailable;
  }

}
