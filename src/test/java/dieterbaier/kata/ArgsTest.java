package dieterbaier.kata;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import dieterbaier.kata.args.ArgumentParser;
import dieterbaier.kata.args.BooleanArgument;
import dieterbaier.kata.args.IntegerArgument;
import dieterbaier.kata.args.StringArgument;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ArgsTest {

  private ArgumentParser  argumentParser;

  private BooleanArgument loggingArgument;

  private IntegerArgument portArgument;

  private StringArgument  pathArgument;

  @BeforeMethod
  public void beforeMethod() {
    argumentParser = new ArgumentParser();
    loggingArgument = new BooleanArgument(false, true);
    portArgument = new IntegerArgument(0, -1);
    pathArgument = new StringArgument("", "deleted");
    argumentParser.addFlag('l', loggingArgument);
    argumentParser.addFlag('p', portArgument);
    argumentParser.addFlag('d', pathArgument);
    assertThat(argumentParser.getValue('l'), is("false"));
    assertThat(argumentParser.getValue('p'), is("0"));
    assertThat(argumentParser.getValue('d'), is(""));
  }

  @Test
  public void booleanFlagNotGiven() {
    argumentParser.parse(new String[] { "-p", "8080", "-d", "/any/dir" });
    assertThat(argumentParser.getValue('l'), is("false"));
    assertThat(loggingArgument.getValue(), is(false));
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void booleanFlagTwice() {
    argumentParser.parse(new String[] { "-l", "-p", "8080", "-d", "/any/dir", "-l", "true" });
  }

  @Test
  public void booleanFlagWithFalseArgument() {
    argumentParser.parse(new String[] { "-l", "false", "-p", "8080", "-d", "/any/dir" });
    assertThat(argumentParser.getValue('l'), is("false"));
    assertThat(loggingArgument.getValue(), is(false));
  }

  @Test
  public void booleanFlagWithoutArgument() {
    argumentParser.parse(new String[] { "-l", "-p", "8080", "-d", "/any/dir" });
    assertThat(argumentParser.getValue('l'), is("true"));
    assertThat(loggingArgument.getValue(), is(true));
  }

  @Test
  public void booleanFlagWithTrueArgument() {
    argumentParser.parse(new String[] { "-l", "true", "-p", "8080", "-d", "/any/dir" });
    assertThat(argumentParser.getValue('l'), is("true"));
    assertThat(loggingArgument.getValue(), is(true));
  }

  @Test
  public void differentOrder() {
    argumentParser.parse(new String[] { "-d", "/any/dir", "-p", "8080", "-l" });
    assertThat(argumentParser.getValue('d'), is("/any/dir"));
    assertThat(pathArgument.getValue(), is("/any/dir"));
    assertThat(argumentParser.getValue('p'), is("8080"));
    assertThat(portArgument.getValue(), is(8080));
    assertThat(argumentParser.getValue('l'), is("true"));
    assertThat(loggingArgument.getValue(), is(true));
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
    assertThat(portArgument.getValue(), is(8080));
  }

  @Test
  public void integerFlagWithMinusArgument() {
    argumentParser.parse(new String[] { "-l", "-p", "-8080", "-d", "/any/dir" });
    assertThat(argumentParser.getValue('p'), is("-8080"));
    assertThat(portArgument.getValue(), is(-8080));
  }

  @Test
  public void integerFlagWithoutArgument() {
    argumentParser.parse(new String[] { "-l", "-p", "-d", "/any/dir" });
    assertThat(argumentParser.getValue('p'), is("-1"));
    assertThat(portArgument.getValue(), is(-1));
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void stringFlagTwice() {
    argumentParser.parse(new String[] { "-l", "-p", "8080", "-d", "-d" });
  }

  @Test
  public void stringFlagWithArgument() {
    argumentParser.parse(new String[] { "-l", "-p", "8080", "-d", "/any/dir" });
    assertThat(argumentParser.getValue('d'), is("/any/dir"));
  }

  @Test
  public void stringFlagWithArgumentLookingLikeFlag() {
    argumentParser.parse(new String[] { "-l", "-p", "8080", "-d", "-x" });
    assertThat(argumentParser.getValue('d'), is("-x"));
  }

  @Test
  public void stringFlagWithoutArgument() {
    argumentParser.parse(new String[] { "-l", "-p", "8080", "-d" });
    assertThat(argumentParser.getValue('d'), is("deleted"));
    assertThat(pathArgument.getValue(), is("deleted"));
  }
}
