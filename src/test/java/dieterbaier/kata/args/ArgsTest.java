package dieterbaier.kata.args;

import static org.testng.AssertJUnit.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ArgsTest {

  private ArgumentParser parser;

  @BeforeMethod
  public void beforeMethod() {
    parser = new ArgumentParser();
    parser.addFlag('l');
    parser.addFlag('p').withIntegerValue();
    parser.addFlag('d').withStringValue();
  }

  @Test
  public void ArgumentsInCorrectOrder() {
    parser.parse(new String[] { "-l", "-p", "8080", "-d", "/usr/logs" });
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void ArgumentsInIncorrectOrder() {
    parser.parse(new String[] { "-p", "8080", "-l", "-d", "/usr/logs" });
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void TooManyArguments() {
    parser.parse(new String[] { "-l", "-p", "8080", "-d", "/usr/logs", "-x" });
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void IllegalValue() {
    parser.parse(new String[] { "-l", "-p", "xxx", "-d", "/usr/logs" });
  }

  @Test
  public void DefaultValueForBooleanFlag() {
    parser.parse(new String[] { "-p", "8080", "-d", "/usr/logs" });
    Object value = parser.getValue('l');
    assertTrue(value instanceof Boolean);
    assertFalse((Boolean) value);
  }
  
  @Test
  public void ValueOfLFlag() {
    parser.parse(new String[] { "-l", "-p", "8080", "-d", "/usr/logs" });
    Object value = parser.getValue('l');
    assertTrue(value instanceof Boolean);
    assertTrue((Boolean) value);
  }
  
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void IllegalValueOfBooleanFlag() {
    parser.parse(new String[] { "-l", "false", "-p", "8080", "-d", "/usr/logs" });
  }
  
  @Test
  public void ValueOfPFlag() {
    parser.parse(new String[] { "-p", "8080", "-d", "/usr/logs" });
    Object value = parser.getValue('p');
    assertTrue(value instanceof Integer);
    assertEquals(new Integer(8080), (Integer)value);
  }
  
  @Test
  public void ValueOfPFlagWithNegativeInteger() {
    parser.parse(new String[] { "-p", "-1", "-d", "/usr/logs" });
    Object value = parser.getValue('p');
    assertTrue(value instanceof Integer);
    assertEquals(new Integer(-1), (Integer)value);
  }
  
  @Test
  public void ValueOfDFlag() {
    parser.parse(new String[] { "-p", "8080", "-d", "/usr/logs" });
    Object value = parser.getValue('d');
    assertTrue(value instanceof String);
    assertEquals("/usr/logs", (String)value);
  }
  
  @Test
  public void DefaultValueWithIntegerFlag() {
    parser.parse(new String[] {"-d", "/usr/logs" });
    Object value = parser.getValue('p');
    assertTrue(value instanceof Integer);
    assertEquals(new Integer(0), (Integer)value);
  }
  
  @Test
  public void DefaultValueWithStringFlag() {
    parser.parse(new String[] { });
    Object value = parser.getValue('d');
    assertTrue(value instanceof String);
    assertEquals("", (String)value);
  }

}
