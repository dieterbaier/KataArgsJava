package dieterbaier.kata.args;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArgumentParser {

  private static final String               BLANKPLACEHOLDER = "%20";

  private final Map<Character, Argument<?>> allreadySetFlags = new HashMap<Character, Argument<?>>();

  public ArgumentParser() {}

  public void addFlag(final char flag, final Argument<?> argument) {
    allreadySetFlags.put(flag, argument);
  }

  public String getValue(final char flag) {
    return Arrays.toString(allreadySetFlags.get(flag).getValue()).replaceAll("\\[", "").replaceAll("\\]", "")
        .replaceAll(", ", ",");
  }

  public String[] getValues(final char flag) {
    return getValue(flag).split(",");
  }

  public void parse(final String argumentList) {
    String manipulatedList = argumentList;
    final Matcher parser = Pattern.compile("\".*\"").matcher(argumentList);
    while (parser.find()) {
      final String originalText = parser.group();
      final String newText = originalText.replaceAll("\"", "").replaceAll(" ", BLANKPLACEHOLDER);
      manipulatedList = manipulatedList.replace(originalText, newText);
    }
    parse(manipulatedList.split(" "));
  }

  public void parse(final String[] args) {
    final Set<Character> allreadyHandledFlags = new HashSet<>();
    // we need to use this kind of for-loop because in case one argument belongs to the other argument, the loop must
    // skip this argument
    for (int indexOfCurrentArgument = 0; indexOfCurrentArgument < args.length; indexOfCurrentArgument++) {
      final String potentialFlag = args[indexOfCurrentArgument];
      final String nextGivenArgument = isOneMoreArgumentAvailable(args, indexOfCurrentArgument) ? getNextArgument(args,
          indexOfCurrentArgument) : null;
      final char flagsKey = isFlag(potentialFlag) ? getFlagsKey(potentialFlag) : ' ';
      checkIfFlagWasAllreadyHandled(allreadyHandledFlags, flagsKey);
      if (isNextArgumentIsPartOfGivenFlag(getAllreadySetFlag(flagsKey), nextGivenArgument))
        // we skip the next argument, since it belonged to the current one and should not be handled by the loop
        // therefore
        indexOfCurrentArgument++;
    }
  }

  private void checkIfFlagWasAllreadyHandled(final Set<Character> allreadyHandledFlags, final char currentFlagToHandle) {
    if (allreadyHandledFlags.contains(currentFlagToHandle))
      throw new IllegalArgumentException("Flag " + currentFlagToHandle + " twice in the argument-list");
    else
      allreadyHandledFlags.add(currentFlagToHandle);
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
    return argument != null && argument.length() == 2 && argument.startsWith("-")
        && allreadySetFlags.containsKey(getFlagsKey(argument));
  }

  private boolean isNextArgumentAFlagsArgument(final String nextArgument) {
    return nextArgument != null && !isFlag(nextArgument);
  }

  private boolean isNextArgumentIsPartOfGivenFlag(final Argument<?> argumentOfGivenFlag, final String nextArgument) {
    if (isNextArgumentAFlagsArgument(nextArgument)) {
      final String[] values = nextArgument.split(",");
      for (final String value : values)
        argumentOfGivenFlag.setValue(value.replaceAll(BLANKPLACEHOLDER, " "));
      return true;
    }
    else {
      argumentOfGivenFlag.setValue(argumentOfGivenFlag.getDefaultValueForSetFlag());
      return false;
    }
  }

  private boolean isOneMoreArgumentAvailable(final String[] args, final int indexOfCurrentArgument) {
    return indexOfCurrentArgument < args.length - 1;
  }

}
