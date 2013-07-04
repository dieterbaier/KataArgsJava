package dieterbaier.kata;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import dieterbaier.kata.args.Argument;
import dieterbaier.kata.args.ArgumentParser;
import dieterbaier.kata.args.BooleanArgument;
import dieterbaier.kata.args.IntegerArgument;
import dieterbaier.kata.args.StringArgument;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ArgsTest {

  private ArgumentParser    argumentParser;

  private Argument<Boolean> booleanArgument;

  private IntegerArgument   integerArgument;

  private StringArgument    stringArgument;

  @BeforeMethod
  public void beforeMethod() {
    argumentParser = new ArgumentParser();
    booleanArgument = new BooleanArgument(false, true);
    integerArgument = new IntegerArgument(0, -1);
    stringArgument = new StringArgument("", "deleted");
    argumentParser.addFlag('l', booleanArgument);
    argumentParser.addFlag('p', integerArgument);
    argumentParser.addFlag('d', stringArgument);
  }

  @Test
  public void booleanFlagNotGiven() {
    argumentParser.parse(new String[] { "-p", "8080", "-d", "/any/dir" });
    assertThat(argumentParser.getValue('l'), is("false"));
    checkTypedValue(booleanArgument.getValue(), 1, false);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void booleanFlagTwice() {
    argumentParser.parse(new String[] { "-l", "-p", "8080", "-d", "/any/dir", "-l", "true" });
  }

  @Test
  public void booleanFlagWithArrayArgument() {
    argumentParser.parse(new String[] { "-l", "false,true,false", "-p", "8080", "-d", "/any/dir" });
    assertThat(argumentParser.getValue('l'), is("false,true,false"));
    final String[] values = argumentParser.getValues('l');
    assertThat(values.length, is(3));
    assertThat(values[0], is("false"));
    assertThat(values[1], is("true"));
    assertThat(values[2], is("false"));
    checkTypedValue(booleanArgument.getValue(), 3, false, true, false);
  }

  @Test
  public void booleanFlagWithFalseArgument() {
    argumentParser.parse(new String[] { "-l", "false", "-p", "8080", "-d", "/any/dir" });
    assertThat(argumentParser.getValue('l'), is("false"));
    checkTypedValue(booleanArgument.getValue(), 1, false);
  }

  @Test
  public void booleanFlagWithoutArgument() {
    argumentParser.parse(new String[] { "-l", "-p", "8080", "-d", "/any/dir" });
    assertThat(argumentParser.getValue('l'), is("true"));
    checkTypedValue(booleanArgument.getValue(), 1, true);
  }

  @Test
  public void booleanFlagWithTrueArgument() {
    argumentParser.parse(new String[] { "-l", "true", "-p", "8080", "-d", "/any/dir" });
    assertThat(argumentParser.getValue('l'), is("true"));
    checkTypedValue(booleanArgument.getValue(), 1, true);
  }

  @Test
  public void checkInitializedParser() {
    assertThat(argumentParser.getValue('l'), is("false"));
    checkTypedValue(booleanArgument.getValue(), 1, false);
    assertThat(argumentParser.getValue('p'), is("0"));
    checkTypedValue(integerArgument.getValue(), 1, 0);
    assertThat(argumentParser.getValue('d'), is(""));
    checkTypedValue(stringArgument.getValue(), 1, "");
  }

  @Test
  public void differentOrder() {
    argumentParser.parse(new String[] { "-d", "/any/dir", "-p", "8080", "-l" });
    assertThat(argumentParser.getValue('d'), is("/any/dir"));
    checkTypedValue(stringArgument.getValue(), 1, "/any/dir");
    assertThat(argumentParser.getValue('p'), is("8080"));
    checkTypedValue(integerArgument.getValue(), 1, 8080);
    assertThat(argumentParser.getValue('l'), is("true"));
    checkTypedValue(booleanArgument.getValue(), 1, true);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void illegalFlag() {
    argumentParser.parse(new String[] { "-x", "-d", "/any/dir", "-p", "8080", "-l" });
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void illegalLoggingFlag() {
    argumentParser.parse(new String[] { "-l", "xxxx", "-p", "-d", "/any/dir" });
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void illegalPortFlag() {
    argumentParser.parse(new String[] { "-l", "-p", "xyz", "-d", "/any/dir" });
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void illegalStartOfArguments() {
    argumentParser.parse(new String[] { "illegalArgumentAtBeginning", "-d", "/any/dir", "-p", "8080", "-l" });
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void integerFlagTwice() {
    argumentParser.parse(new String[] { "-p", "-l", "-p", "8080", "-d", "/any/dir" });
  }

  @Test
  public void integerFlagWithArgument() {
    argumentParser.parse(new String[] { "-l", "-p", "8080", "-d", "/any/dir" });
    assertThat(argumentParser.getValue('p'), is("8080"));
    checkTypedValue(integerArgument.getValue(), 1, 8080);
  }

  @Test
  public void integerFlagWithArrayArgument() {
    argumentParser.parse(new String[] { "-l", "-p", "8080,4711", "-d", "/any/dir" });
    assertThat(argumentParser.getValue('p'), is("8080,4711"));
    checkTypedValue(integerArgument.getValue(), 2, 8080, 4711);
  }

  @Test
  public void integerFlagWithMinusArgument() {
    argumentParser.parse(new String[] { "-l", "-p", "-8080", "-d", "/any/dir" });
    assertThat(argumentParser.getValue('p'), is("-8080"));
    checkTypedValue(integerArgument.getValue(), 1, -8080);
  }

  @Test
  public void integerFlagWithoutArgument() {
    argumentParser.parse(new String[] { "-l", "-p", "-d", "/any/dir" });
    assertThat(argumentParser.getValue('p'), is("-1"));
    checkTypedValue(integerArgument.getValue(), 1, -1);
  }

  @Test
  public void parsingStringAsArgumentList() {
    argumentParser.parse("-d \"/any/dir,/any other/dir\" -p 8080,4711 -l true,false");
    assertThat(argumentParser.getValue('d'), is("/any/dir,/any other/dir"));
    checkTypedValue(stringArgument.getValue(), 2, "/any/dir", "/any other/dir");
    assertThat(argumentParser.getValue('p'), is("8080,4711"));
    checkTypedValue(integerArgument.getValue(), 2, 8080, 4711);
    assertThat(argumentParser.getValue('l'), is("true,false"));
    checkTypedValue(booleanArgument.getValue(), 2, true, false);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void stringFlagTwice() {
    argumentParser.parse(new String[] { "-l", "-p", "8080", "-d", "-d" });
  }

  @Test
  public void stringFlagWithArgument() {
    argumentParser.parse(new String[] { "-l", "-p", "8080", "-d", "/any/dir" });
    assertThat(argumentParser.getValue('d'), is("/any/dir"));
    checkTypedValue(stringArgument.getValue(), 1, "/any/dir");
  }

  @Test
  public void stringFlagWithArgumentLookingLikeFlag() {
    argumentParser.parse(new String[] { "-l", "-p", "8080", "-d", "-x" });
    assertThat(argumentParser.getValue('d'), is("-x"));
  }

  @Test
  public void stringFlagWithArrayArgument() {
    argumentParser.parse(new String[] { "-l", "-p", "8080", "-d", "/any/dir,/any oder/dir" });
    assertThat(argumentParser.getValue('d'), is("/any/dir,/any oder/dir"));
    checkTypedValue(stringArgument.getValue(), 2, "/any/dir", "/any oder/dir");
  }

  @Test
  public void stringFlagWithBlankInArgument() {
    argumentParser.parse("-l -p 8080 -d \"/any/dir and/subdir\"");
    assertThat(argumentParser.getValue('d'), is("/any/dir and/subdir"));
    checkTypedValue(stringArgument.getValue(), 1, "/any/dir and/subdir");
  }

  @Test
  public void stringFlagWithoutArgument() {
    argumentParser.parse(new String[] { "-l", "-p", "8080", "-d" });
    assertThat(argumentParser.getValue('d'), is("deleted"));
    checkTypedValue(stringArgument.getValue(), 1, "deleted");
  }

  private <T> void checkTypedValue(final T[] values, final int expectedValuesSize,
      final T... expectedValues) {
    assertThat(values.length, is(expectedValuesSize));
    assertThat(values.length, is(expectedValues.length));
    for (int i = 0; i < expectedValuesSize; i++)
      assertThat(values[i], is(expectedValues[i]));
  }
}
