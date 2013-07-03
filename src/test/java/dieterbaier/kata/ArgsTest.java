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
    argumentParser.addFlag('l', false, true);
    argumentParser.addFlag('p', 0, 0);
    argumentParser.addFlag('d', "", "");
  }

  @Test
  public void differentOrder() {
    argumentParser.parse(new String[] { "-d", "/any/dir", "-p", "8080", "-l" });
    assertThat(argumentParser.get('d'), is("/any/dir"));
    assertThat(argumentParser.get('p'), is("8080"));
    assertThat(argumentParser.get('l'), is("true"));
  }

  @Test
  public void directoryFlagWithArgument() {
    argumentParser.parse(new String[] { "-l", "-p", "8080", "-d", "/any/dir" });
    assertThat(argumentParser.get('d'), is("/any/dir"));
  }

  @Test
  public void directoryFlagWithoutArgument() {
    argumentParser.parse(new String[] { "-l", "-p", "8080", "-d" });
    assertThat(argumentParser.get('d'), is(""));
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void illegalLoggingFlag() {
    argumentParser.parse(new String[] { "-l", "xxxx", "-p", "-d", "/any/dir" });
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void illegalPortFlag() {
    argumentParser.parse(new String[] { "-l", "-p", "xyz", "-d", "/any/dir" });
  }

  @Test
  public void loggingFlagWithFalseArgument() {
    argumentParser.parse(new String[] { "-l", "false", "-p", "8080", "-d", "/any/dir" });
    assertThat(argumentParser.get('l'), is("false"));
  }

  @Test
  public void loggingFlagWithoutArgument() {
    argumentParser.parse(new String[] { "-l", "-p", "8080", "-d", "/any/dir" });
    assertThat(argumentParser.get('l'), is("true"));
  }

  @Test
  public void loggingFlagWithTrueArgument() {
    argumentParser.parse(new String[] { "-l", "true", "-p", "8080", "-d", "/any/dir" });
    assertThat(argumentParser.get('l'), is("true"));
  }

  @Test
  public void noLoggingFlag() {
    argumentParser.parse(new String[] { "-p", "8080", "-d", "/any/dir" });
    assertThat(argumentParser.get('l'), is("false"));
  }

  @Test
  public void portFlagWithArgument() {
    argumentParser.parse(new String[] { "-l", "-p", "8080", "-d", "/any/dir" });
    assertThat(argumentParser.get('p'), is("8080"));
  }

  @Test
  public void portFlagWithMinusArgument() {
    argumentParser.parse(new String[] { "-l", "-p", "-8080", "-d", "/any/dir" });
    assertThat(argumentParser.get('p'), is("-8080"));
  }

  @Test
  public void portFlagWithoutArgument() {
    argumentParser.parse(new String[] { "-l", "-p", "-d", "/any/dir" });
    assertThat(argumentParser.get('p'), is("0"));
  }
}
