package dieterbaier.kata.args;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArgumentParser {

  private final Map<Character, Argument<?>> arguments        = new HashMap<>();

  private final String                      BLANKPLACEHOLDER = "%20";

  public void addBooleanFlag(final char key, final boolean defaultValue) {
    arguments.put(key, new BooleanArgument(key, defaultValue));
  }

  public void addIntegerFlag(final char key, final int defaultValue) {
    arguments.put(key, new IntegerArgument(key, defaultValue));
  }

  public void addStringFlag(final char key, final String defaultValue) {
    arguments.put(key, new StringArgument(key, defaultValue));
  }

  public String[] getAllValues(final char key) {
    return arguments.get(key).getValues();
  }

  public String getValue(final char key) {
    final Argument<?> argument = arguments.get(key);
    return argument.getValue().replaceAll(BLANKPLACEHOLDER, " ");
  }

  public void parse(final String arguments) {
    String manipulatedArguments = arguments;
    final Matcher parser = Pattern.compile("\".*\"").matcher(manipulatedArguments);
    while (parser.find()) {
      final String origString = parser.group();
      manipulatedArguments = manipulatedArguments.replaceAll(origString, origString.replaceAll(" ", BLANKPLACEHOLDER))
          .replaceAll("\"", "");
    }
    parse(manipulatedArguments.split(" "));
  }

  public void parse(final String[] args) {
    for (int i = 0; i < args.length; i++) {
      final String nextArg = i < args.length - 1 ? args[i + 1] : "";
      if (isFlagWithArgument(args[i], nextArg))
        i++;
    }
  }

  private char extractKeyValue(final String argument) {
    return argument.substring(1).charAt(0);
  }

  private boolean isFlag(final String argument) {
    if (argument == null || argument.length() != 2 || !argument.startsWith("-"))
      return false;
    return arguments.keySet().contains(extractKeyValue(argument));
  }

  private boolean isFlagWithArgument(final String currentArg, final String nextArg) {
    boolean isArgumentForFlagAvailable = false;

    if (!isFlag(currentArg))
      throw new IllegalArgumentException("Invalid Flag");

    final Argument<?> argument = arguments.get(extractKeyValue(currentArg));
    if (!nextArg.isEmpty() && !isFlag(nextArg)) {
      argument.setValue(nextArg);
      isArgumentForFlagAvailable = true;
    }
    else
      argument.setDefaultValueForGivenFlag();

    return isArgumentForFlagAvailable;
  }

}
