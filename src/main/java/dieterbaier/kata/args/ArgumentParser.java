package dieterbaier.kata.args;

import java.util.HashMap;
import java.util.Map;

public class ArgumentParser {

  private final Map<Character, Argument<?>> allreadySetFlags = new HashMap<Character, Argument<?>>();

  public ArgumentParser() {}

  public void addFlag(final char flag, final Argument<?> argument) {
    allreadySetFlags.put(flag, argument);
  }

  public String getValue(final char flag) {
    return allreadySetFlags.get(flag).getValueAsString();
  }

  public void parse(final String[] args) {
    // we need to use this kind of for-loop because in case one argument belongs to the other argument, the loop must
    // skip this argument
    for (int indexOfCurrentArgument = 0; indexOfCurrentArgument < args.length; indexOfCurrentArgument++) {
      final String potentialFlag = args[indexOfCurrentArgument];
      final String nextGivenArgument = isOneMoreArgumentAvailable(args, indexOfCurrentArgument) ? getNextArgument(args,
          indexOfCurrentArgument) : null;
      final char flagsKey = isFlag(potentialFlag) ? getFlagsKey(potentialFlag) : ' ';
      if (isNextArgumentIsPartOfGivenFlag(getAllreadySetFlag(flagsKey), nextGivenArgument))
        // we skip the next argument, since it belonged to the current one and should not be handled by the loop
        // therefore
        indexOfCurrentArgument++;
    }
  }

  private Argument<?> getAllreadySetFlag(final char flag) {
    if (!allreadySetFlags.keySet().contains(flag))
      throw new IllegalArgumentException("Illegal flag" + flag);
    return allreadySetFlags.get(flag);
  }

  private char getFlagsKey(final String potentialFlag) {
    return potentialFlag.charAt(1);
  }

  private String getNextArgument(final String[] args, final int indexOfCurrentArgument) {
    return args[indexOfCurrentArgument + 1];
  }

  private boolean isFlag(final String argument) {
    return argument != null && argument.length() == 2 && argument.startsWith("-");
  }

  private boolean isNextArgumentIsPartOfGivenFlag(final Argument<?> argumentOfGivenFlag, final String nextArgument) {
    if (isNextArgumentAFlagsArgument(nextArgument)) {
      argumentOfGivenFlag.setValue(nextArgument);
      return true;
    }
    else {
      argumentOfGivenFlag.setValue(argumentOfGivenFlag.getDefaultValueForSetFlag());
      return false;
    }
  }

  private boolean isNextArgumentAFlagsArgument(final String nextArgument) {
    return nextArgument != null && !isFlag(nextArgument);
  }

  private boolean isOneMoreArgumentAvailable(final String[] args, final int indexOfCurrentArgument) {
    return indexOfCurrentArgument < args.length - 1;
  }

}
