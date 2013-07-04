package dieterbaier.kata.args;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArgumentParser {

  private static final String               CLOSINGBRACKET   = "\\]";

  private static final String               OPENINGBRACKET   = "\\[";

  private static final String               MINUSSIGN        = "-";

  private static final String               COMMA            = ",";

  private static final String               BLANK            = " ";

  private static final String               NOTHING          = "";

  private static final String               ANYCHARACTERS    = ".*";

  private static final String               APOSTROPHE       = "\"";

  private static final String               BLANKPLACEHOLDER = "%20";

  private final Map<Character, Argument<?>> allreadySetFlags = new HashMap<Character, Argument<?>>();

  public ArgumentParser() {}

  public void addFlag(final char flag, final Argument<?> argument) {
    allreadySetFlags.put(flag, argument);
  }

  public String getValue(final char flag) {
    return removeUnnaccassaryCharsFromArrayRepresantation(Arrays.toString(getFlagsArgumentValue(flag)));
  }

  public String[] getValues(final char flag) {
    return getValue(flag).split(COMMA);
  }

  public void parse(final String argumentList) {
    String manipulatedList = argumentList;
    final Matcher parser = Pattern.compile(APOSTROPHE + ANYCHARACTERS + APOSTROPHE).matcher(argumentList);
    while (parser.find()) {
      final String originalText = parser.group();
      final String newText = originalText.replaceAll(APOSTROPHE, NOTHING).replaceAll(BLANK, BLANKPLACEHOLDER);
      manipulatedList = manipulatedList.replace(originalText, newText);
    }
    parse(manipulatedList.split(BLANK));
  }

  public void parse(final String[] args) {
    final Set<Character> allreadyHandledFlags = new HashSet<>();
    // we need to use this kind of for-loop because in case one argument belongs to the other argument, the loop must
    // skip this argument
    for (int indexOfCurrentArgument = 0; indexOfCurrentArgument < args.length; indexOfCurrentArgument++) {
      final String potentialFlag = args[indexOfCurrentArgument];
      final String nextGivenArgument = isOneMoreArgumentAvailable(args, indexOfCurrentArgument) ? getNextArgument(args,
          indexOfCurrentArgument) : null;
      final char flagsKey = isFlag(potentialFlag) ? getFlagsKey(potentialFlag) : BLANK.charAt(0);
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

  private Object[] getFlagsArgumentValue(final char flag) {
    return allreadySetFlags.get(flag).getValue();
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
    return argument != null && argument.length() == 2 && argument.startsWith(MINUSSIGN)
        && allreadySetFlags.containsKey(getFlagsKey(argument));
  }

  private boolean isNextArgumentAFlagsArgument(final String nextArgument) {
    return nextArgument != null && !isFlag(nextArgument);
  }

  private boolean isNextArgumentIsPartOfGivenFlag(final Argument<?> argumentOfGivenFlag, final String nextArgument) {
    if (isNextArgumentAFlagsArgument(nextArgument)) {
      final String[] values = nextArgument.split(COMMA);
      for (final String value : values)
        argumentOfGivenFlag.setValue(value.replaceAll(BLANKPLACEHOLDER, BLANK));
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

  private String removeUnnaccassaryCharsFromArrayRepresantation(final String arrayAsString) {
    return arrayAsString.replaceAll(OPENINGBRACKET, NOTHING)
        .replaceAll(CLOSINGBRACKET, NOTHING)
        .replaceAll(COMMA + BLANK, COMMA);
  }

}
