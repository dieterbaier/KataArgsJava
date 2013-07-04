package dieterbaier.kata.args;

import java.util.HashMap;
import java.util.Map;

public class ArgumentParser {

  private final Map<Character, Argument<?>> arguments = new HashMap<Character, Argument<?>>();

  public ArgumentParser() {}

  public void addFlag(final char key, final Argument<?> argument) {
    arguments.put(key, argument);
  }

  public String getValue(final char key) {
    return arguments.get(key).getValueAsString();
  }

  public void parse(final String[] args) {
    // we need to use this kind of for-loop because in case one argument belongs to the other argument, the loop must
    // skip this argument
    for (int indexOfCurrentArgument = 0; indexOfCurrentArgument < args.length; indexOfCurrentArgument++) {
      final String potentialFlag = args[indexOfCurrentArgument];
      final String nextGivenArgument = isOneMoreArgumentAvailable(args, indexOfCurrentArgument) ? getNextArgument(args,
          indexOfCurrentArgument) : null;
      final char key = isFlag(potentialFlag) ? getFlagValue(potentialFlag) : ' ';
      final boolean isNextArgumentIsPartOfGivenArgument = setValueForCurrentFlag(key, nextGivenArgument);
      if (isNextArgumentIsPartOfGivenArgument)
        // we skip the next argument, since it belonged to the current one and should not be handled by the loop
        // therefore
        indexOfCurrentArgument++;
    }
  }

  private Argument<?> getAllreadySetArgument(final char key) {
    if (!arguments.keySet().contains(key))
      throw new IllegalArgumentException("Illegal flag" + key);
    return arguments.get(key);
  }

  private char getFlagValue(final String potentialFlag) {
    return potentialFlag.charAt(1);
  }

  private String getNextArgument(final String[] args, final int indexOfCurrentArgument) {
    return args[indexOfCurrentArgument + 1];
  }

  private boolean isFlag(final String argument) {
    return argument != null && argument.length() == 2 && argument.startsWith("-");
  }

  private boolean isNextArgumentPartOfThisArgument(final String nextGivenArgument) {
    return nextGivenArgument != null && !isFlag(nextGivenArgument);
  }

  private boolean isOneMoreArgumentAvailable(final String[] args, final int indexOfCurrentArgument) {
    return indexOfCurrentArgument < args.length - 1;
  }

  private boolean setValueForCurrentFlag(final char key, final String nextGivenArgument) {
    final Argument<?> currentArgument = getAllreadySetArgument(key);
    if (isNextArgumentPartOfThisArgument(nextGivenArgument)) {
      currentArgument.setValue(nextGivenArgument);
      return true;
    }
    else {
      currentArgument.setValue(currentArgument.getDefaultValueForSetFlag());
      return false;
    }
  }

}
