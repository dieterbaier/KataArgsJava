package dieterbaier.kata;

import dieterbaier.kata.args.ArgumentParser;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import static org.testng.AssertJUnit.*;
import org.testng.annotations.BeforeMethod;

/**
 * Following tests should be given:<br>
 * - (group1) make sure you have a test with a negative integer (confusing - sign)<br>
 * - (group2) the order of the arguments need not match the order given in the schema.<br>
 * - (group3) have some tests that suitable default values are correctly assigned if flags given in the schema are
 * missing in the args given.
 */
public class ArgsTest {

  private ArgumentParser parser;

  @BeforeMethod
  public void beforeMethod() {
    parser = new ArgumentParser();
    parser.addFlag('l');
    parser.addFlag('p').withIntegerValue();
    parser.addFlag('d').withStringValue();
  }

  @Test(groups = { "all" }, expectedExceptions = IllegalArgumentException.class)
  public void IllegalValue() {
    parser.parse(new String[] { "-l", "-p", "xxx", "-d", "/usr/logs" });
  }

  @Test(groups = { "all" })
  public void ValueOfLFlag() {
    parser.parse(new String[] { "-l", "-p", "8080", "-d", "/usr/logs" });
    Object value = parser.getValue('l');
    AssertJUnit.assertTrue(value instanceof Boolean);
    assertTrue((Boolean) value);
  }

  @Test(groups = { "all" }, expectedExceptions = IllegalArgumentException.class)
  public void IllegalValueOfBooleanFlag() {
    parser.parse(new String[] { "-l", "false", "-p", "8080", "-d", "/usr/logs" });
  }

  @Test(groups = { "all" })
  public void ValueOfPFlag() {
    parser.parse(new String[] { "-p", "8080", "-d", "/usr/logs" });
    Object value = parser.getValue('p');
    AssertJUnit.assertTrue(value instanceof Integer);
    AssertJUnit.assertEquals(new Integer(8080), (Integer) value);
  }

  @Test(groups = { "all" })
  public void ValueOfDFlag() {
    parser.parse(new String[] { "-p", "8080", "-d", "/usr/logs" });
    Object value = parser.getValue('d');
    AssertJUnit.assertTrue(value instanceof String);
    AssertJUnit.assertEquals("/usr/logs", (String) value);
  }

  @Test(groups = { "group1" })
  public void ValueOfPFlagWithNegativeInteger() {
    parser.parse(new String[] { "-p", "-1", "-d", "/usr/logs" });
    Object value = parser.getValue('p');
    AssertJUnit.assertTrue(value instanceof Integer);
    AssertJUnit.assertEquals(new Integer(-1), (Integer) value);
  }

  @Test(groups = { "group2" })
  public void ArgumentsInCorrectOrder() {
    parser.parse(new String[] { "-l", "-p", "8080", "-d", "/usr/logs" });
  }

  @Test(groups = { "group2" }, expectedExceptions = IllegalArgumentException.class)
  public void ArgumentsInIncorrectOrder() {
    parser.parse(new String[] { "-p", "8080", "-l", "-d", "/usr/logs" });
  }

  @Test(groups = { "group2" }, expectedExceptions = IllegalArgumentException.class)
  public void TooManyArguments() {
    parser.parse(new String[] { "-l", "-p", "8080", "-d", "/usr/logs", "-x" });
  }

  @Test(groups = { "group3" })
  public void DefaultValueWithIntegerFlag() {
    parser.parse(new String[] { "-d", "/usr/logs" });
    Object value = parser.getValue('p');
    AssertJUnit.assertTrue(value instanceof Integer);
    AssertJUnit.assertEquals(new Integer(0), (Integer) value);
  }

  @Test(groups = { "group3" })
  public void DefaultValueWithStringFlag() {
    parser.parse(new String[] {});
    Object value = parser.getValue('d');
    AssertJUnit.assertTrue(value instanceof String);
    AssertJUnit.assertEquals("", (String) value);
  }

  @Test(groups = { "group3" })
  public void DefaultValueForBooleanFlag() {
    parser.parse(new String[] { "-p", "8080", "-d", "/usr/logs" });
    Object value = parser.getValue('l');
    AssertJUnit.assertTrue(value instanceof Boolean);
    assertFalse((Boolean) value);
  }
}
