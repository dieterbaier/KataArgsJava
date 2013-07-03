package dieterbaier.kata;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import dieterbaier.kata.args.ArgumentParser;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ArgsTest {

  private ArgumentParser argumentParser;

  @BeforeMethod
  public void beforeMethod() {
    argumentParser = new ArgumentParser();
    argumentParser.addBooleanFlag("l", false);
    argumentParser.addIntegerFlag("p", 0);
    argumentParser.addStringFlag("d", "");
  }

  @Test
  public void differentOrder() {
    argumentParser.parse(new String[] { "-d", "/any/Path", "-p", "8080", "-l" });
    assertThat(argumentParser.getValue("l"), is("true"));
    assertThat(argumentParser.getValue("d"), is("/any/Path"));
    assertThat(argumentParser.getValue("p"), is("8080"));
  }

  @Test(expectedExceptions = RuntimeException.class)
  public void unknownFlag() {
    argumentParser.parse(new String[] { "-l", "-x", "false", "-p", "8080", "-d", "/any/Path" });
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void invalidLoggingFlag() {
    argumentParser.parse(new String[] { "-l", "abc", "-p", "8080", "-d", "/any/Path" });
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void invalidPortFlag() {
    argumentParser.parse(new String[] { "-l", "-p", "abc", "-d", "/any/path" });
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void invalidStartOfArguments() {
    argumentParser.parse(new String[] { "illegalStart", "-l", "-p", "8080", "-d", "/any/path" });
  }

  @Test
  public void lastFlagWithoutArgument() {
    argumentParser.parse(new String[] { "-l", "-p", "8080", "-d" });
    assertThat(argumentParser.getValue("d"), is(""));
  }

  @Test
  public void loggingFlagWithNoArgument() {
    argumentParser.parse(new String[] { "-l", "-p", "8080", "-d", "/any/Path" });
    assertThat(argumentParser.getValue("l"), is("true"));
  }

  @Test
  public void loggingFlagWithFalseGiven() {
    argumentParser.parse(new String[] { "-l", "false", "-p", "8080", "-d", "/any/Path" });
    assertThat(argumentParser.getValue("l"), is("false"));
  }

  @Test
  public void loggingFlagWithTrueGiven() {
    argumentParser.parse(new String[] { "-l", "true", "-p", "8080", "-d", "/any/Path" });
    assertThat(argumentParser.getValue("l"), is("true"));
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void tooManyArgumentsForOneFlag() {
    argumentParser.parse(new String[] { "-d", "/any/Path", "some/more/path", "-p", "8080", "-l" });
  }

  @Test
  public void noLoggingFlagGiven() {
    argumentParser.parse(new String[] { "-p", "8080", "-d", "/any/Path" });
    assertThat(argumentParser.getValue("l"), is("false"));
  }

  @Test
  public void noDirectoryFlagGiven() {
    argumentParser.parse(new String[] { "-l", "-p", "8080" });
    assertThat(argumentParser.getValue("d"), is(""));
  }

  @Test
  public void noPortFlagGiven() {
    argumentParser.parse(new String[] { "-l", "-d", "/any/Path" });
    assertThat(argumentParser.getValue("p"), is("0"));
  }

  @Test
  public void directoryFlagSet() {
    argumentParser.parse(new String[] { "-l", "-p", "8080", "-d", "/any/Path" });
    assertThat(argumentParser.getValue("d"), is("/any/Path"));
  }

  @Test
  public void directoryFlagsArgumentWithBlank() {
    argumentParser.parse(new String[] { "-d", "/any/Path inDirectory", "-p", "8080", "-l" });
    assertThat(argumentParser.getValue("d"), is("/any/Path inDirectory"));
  }

  @Test
  public void directoryFlagWithNoArgumentGiven() {

    argumentParser.parse(new String[] { "-l", "-d", "-p", "8080" });
    assertThat(argumentParser.getValue("d"), is(""));

  }

  @Test
  public void portFlagSet() {
    argumentParser.parse(new String[] { "-l", "-p", "8080", "-d", "/any/Path" });
    assertThat(argumentParser.getValue("p"), is("8080"));
  }

  @Test
  public void portFlagWithNoArgument() {
    argumentParser.parse(new String[] { "-l", "-p", "-d", "/any/path" });
    assertThat(argumentParser.getValue("p"), is("0"));
  }
}
