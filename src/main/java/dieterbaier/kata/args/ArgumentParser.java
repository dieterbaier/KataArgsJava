package dieterbaier.kata.args;

import dieterbaier.kata.args.InvalidArgumentException.Reasons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArgumentParser {

  private final List<ArgumentFlag>           validFlags = new ArrayList<>(0);

  private final Map<Character, ArgumentFlag> arguments  = new HashMap<Character, ArgumentFlag>();

  public ArgumentFlag addFlag(final char name) {
    ArgumentFlag flag = new ArgumentFlag(name);
    validFlags.add(flag);
    return flag;
  }

  public void parse(final String[] args) throws InvalidArgumentException {
    final List<ArgumentFlag> givenFlags = new ArrayList<>(0);

    convertToArgumentFlags(args, givenFlags);

    if (givenFlags.size() == 0)
      saveArguments(validFlags);
    else {
      synchronizeWithValidFlags(givenFlags);
      saveArguments(givenFlags);
    }

  }

  private void saveArguments(final List<ArgumentFlag> givenFlags) {
    for (int i = 0; i < givenFlags.size(); i++) {
      arguments.put(givenFlags.get(i).name(), givenFlags.get(i));
    }
  }

  private void synchronizeWithValidFlags(final List<ArgumentFlag> givenFlags) {
    checkSize(givenFlags);

    int validFlagsStartPosition = 0;
    for (int i = 0; i < givenFlags.size(); i++) {
      ArgumentFlag givenFlag = givenFlags.get(i);
      for (int j = validFlagsStartPosition; j < validFlags.size(); j++) {
        ArgumentFlag validFlag = validFlags.get(j);
        if (!validFlag.equals(givenFlag)) {
          if (isFlagSomewhereElse(givenFlags, validFlag))
            throw new InvalidArgumentException(Reasons.INCORRECTORDEROFARGUMENTS, "Flag " + givenFlag.name()
                + " is on wrong position.");
          givenFlags.add(i++, validFlag);
          checkSize(givenFlags);
        }
        else {
          validFlagsStartPosition = j + 1;
          break;
        }
      }
    }
  }

  private boolean isFlagSomewhereElse(List<ArgumentFlag> givenFlags, ArgumentFlag validFlag) {
    for (ArgumentFlag givenFlag : givenFlags) {
      if (givenFlag.equals(validFlag))
        return true;
    }
    return false;
  }

  private void convertToArgumentFlags(final String[] args, final List<ArgumentFlag> givenFlags) {
    for (int i = 0; i < args.length; i++) {
      if (ArgumentFlag.isPotentialFlag(args[i])) {
        i = storeFlag(args, givenFlags, i);
      }
    }
  }

  private int storeFlag(final String[] args, final List<ArgumentFlag> flagsStore, final int argsPosition) {
    int newPosition = argsPosition;
    ArgumentFlag flag = convertToFlag(args[newPosition]);
    if (flag.needsValue()) {
      newPosition = setValue(args, argsPosition, flag);
    }
    else {
      setBooleanFlagToTrue(args, newPosition, flag);
    }
    flagsStore.add(flag);
    return newPosition;
  }

  private void setBooleanFlagToTrue(final String[] args, final int argsPosition, final ArgumentFlag booleanFlag) {
    if (isNextElementAFlagOrEndOfArgList(args, argsPosition))
      booleanFlag.setValue(true);
    else
      throw new InvalidArgumentException(Reasons.INVALIDVALUE, "Illegal value for flag " + booleanFlag.name());

  }

  private boolean isNextElementAFlagOrEndOfArgList(final String[] args, final int argsPosition) {
    int nextElementsPosition = argsPosition + 1;
    return nextElementsPosition == args.length
        || (nextElementsPosition < args.length && ArgumentFlag.isPotentialFlag(args[nextElementsPosition]));
  }

  private int setValue(final String[] args, final int flagsPosition, final ArgumentFlag flag) {
    final int valuesPosition = flagsPosition + 1;
    flag.setValue(args[valuesPosition]);
    return valuesPosition;
  }

  private void checkSize(List<ArgumentFlag> givenFlags) {
    if (givenFlags.size() > validFlags.size()) {
      throw new InvalidArgumentException(Reasons.TOOMANYARGUMENTS, "Too many arguments given");
    }
  }

  private ArgumentFlag convertToFlag(final String argument) {
    for (ArgumentFlag validFlag : validFlags) {
      if (validFlag.isValid(argument)) {
        return validFlag;
      }
    }
    return new ArgumentFlag(argument.charAt(1));
  }

  public Object getValue(char name) {
    ArgumentFlag flag = arguments.get(name);
    return flag.value();
  }

}
