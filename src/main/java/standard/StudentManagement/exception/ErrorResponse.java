package standard.StudentManagement.exception;

public class ErrorResponse {
  private String message;
  private String errorCode;

  public ErrorResponse(String message, String errorCode) {
    this.message = message;
    this.errorCode = errorCode;
  }

  public String getMessage() {
    return message;
  }

  public String getErrorCode() {
    return errorCode;
  }
}