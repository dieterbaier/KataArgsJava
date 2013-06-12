package dieterbaier.kata;

import dieterbaier.kata.args.InvalidArgumentException;
import dieterbaier.kata.args.InvalidArgumentException.Reasons;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class InvalidArgumentExceptionMatcher<E extends Throwable> extends BaseMatcher<E> {

  private Reasons exceptedReason;

  private Reasons receivedReason;

  public InvalidArgumentExceptionMatcher(Reasons exceptedReason) {
    this.exceptedReason = exceptedReason;
  }

  @Override
  public boolean matches(Object objectToCheck) {
    if (objectToCheck == null || !(objectToCheck instanceof InvalidArgumentException))
      return false;
    InvalidArgumentException exceptionToCheck = (InvalidArgumentException) objectToCheck;
    receivedReason = exceptionToCheck.getReason();
    return receivedReason == exceptedReason;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("Invalid Reason. Expected: ").appendValue(exceptedReason).appendText("; Received: ")
        .appendValue(receivedReason);
  }

}
