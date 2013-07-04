package dieterbaier.kata.args;

import java.util.HashMap;
import java.util.Map;

public class ArgumentParser {

  private final Map<Character, Argument<?>> arguments = new HashMap<Character, Argument<?>>();

  public ArgumentParser() {}

  public void addFlag(final char key, final Argument<?> argument) {
    arguments.put(key, argument);
  }

  public void addFlag(final char key, final boolean defaultValue, final boolean defaultValueForSetFlag) {
    arguments.put(key, new BooleanArgument(defaultValue, defaultValueForSetFlag));
  }

  public void addFlag(final char key, final Integer defaultValue, final Integer defaultValueForSetFlag) {
    arguments.put(key, new IntegerArgument(defaultValue, defaultValueForSetFlag));
  }

  public void addFlag(final char key, final String defaultValue, final String defaultValueForSetFlag) {
    arguments.put(key, new StringArgument(defaultValue, defaultValueForSetFlag));
  }

  public String getValue(final char key) {
    return arguments.get(key).getValueAsString();
  }

  public void parse(final String[] args) {
    for (int indexOfCurrentArgument = 0; indexOfCurrentArgument < args.length; indexOfCurrentArgument++) {
      final String potentialFlag = args[indexOfCurrentArgument];
      final String nextGivenArgument = isOneMoreArgumentAvailable(args, indexOfCurrentArgument) ? getNextArgument(args, indexOfCurrentArgument) : null;
      final char key = isFlag(potentialFlag) ? getFlagValue(potentialFlag) : ' ';
      final boolean isNextArgumentIsPartOfGivenArgument = setValueForCurrentFlag(key, nextGivenArgument);
      if (isNextArgumentIsPartOfGivenArgument)
        indexOfCurrentArgument++;
    }
  }

  private char getFlagValue(final String potentialFlag) {
    return potentialFlag.charAt(1);
  }

  private String getNextArgument(final String[] args, int indexOfCurrentArgument) {
    return args[indexOfCurrentArgument + 1];
  }

  private boolean isOneMoreArgumentAvailable(final String[] args, int indexOfCurrentArgument) {
    return indexOfCurrentArgument < args.length - 1;
  }

  private Argument<?> getAllreadySetArgument(final char key) {
    if (!arguments.keySet().contains(key))
      throw new IllegalArgumentException("Illegal flag" + key);
    final Argument<?> currentArgument = arguments.get(key);
    return currentArgument;
  }

  private boolean isFlag(final String argument) {
    return argument != null && argument.length() == 2 && argument.startsWith("-");
  }

  private boolean setValueForCurrentFlag(final char key, final String nextGivenArgument) {
    final Argument<?> currentArgument = getAllreadySetArgument(key);
    String value;
    boolean isNextArgumentIsPartOfGivenArgument = false;
    if (nextGivenArgument == null || isFlag(nextGivenArgument))
      value = currentArgument.getDefaultValueForSetFlag();
    else {
      value = nextGivenArgument;
      isNextArgumentIsPartOfGivenArgument = true;
    }
    currentArgument.setValue(value);
    return isNextArgumentIsPartOfGivenArgument;
  }

}
