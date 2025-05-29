package standard.StudentManagement.exception;

public class TestException extends RuntimeException {

  public TestException() {
    super();
  }

  public TestException(String message, Throwable cause) {
    super(message, cause);
  }

  public TestException(Throwable cause) {
    super(cause);
  }

  public TestException(String message) {
    super(message);
  }
}
