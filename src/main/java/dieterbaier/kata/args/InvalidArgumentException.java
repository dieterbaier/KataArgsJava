package dieterbaier.kata.args;


public class InvalidArgumentException extends IllegalArgumentException {

  private Reasons reason = Reasons.UNKNOWN;

  public enum Reasons {
    INVALIDVALUE, UNKNOWN, TOOMANYARGUMENTS, INCORRECTORDEROFARGUMENTS

  }

  private static final long serialVersionUID = 1L;

  public InvalidArgumentException(final Reasons reason, final String message, final Throwable cause) {
    super(message, cause);
    setReason(reason);
  }

  public InvalidArgumentException(Reasons reason, final String message) {
    super(message);
    setReason(reason);
  }

  private void setReason(Reasons reason) {
    if (reason != null)
      this.reason = reason;    
  }

  public Reasons getReason() {
    return reason;
  }
}
