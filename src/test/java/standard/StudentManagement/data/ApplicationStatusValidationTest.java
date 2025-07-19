package standard.StudentManagement.data;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApplicationStatusValidationTest {

  private Validator validator;

  @BeforeEach
  void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void 正常な申込状況の場合はバリデーションエラーにならない() {
    ApplicationStatus status = new ApplicationStatus();
    status.setStatus("仮申込");

    Set<ConstraintViolation<ApplicationStatus>> violations = validator.validate(status);

    assertThat(violations).isEmpty();
  }

  @Test
  void 不正な申込状況の場合はバリデーションエラーになる() {
    ApplicationStatus status = new ApplicationStatus();
    status.setStatus("不正な値");

    Set<ConstraintViolation<ApplicationStatus>> violations = validator.validate(status);

    assertThat(violations).hasSize(1);
    assertThat(violations).anyMatch(v ->
        v.getPropertyPath().toString().equals("status"));
  }

  @Test
  void 申込状況が空文字の場合はバリデーションエラーになる() {
    ApplicationStatus status = new ApplicationStatus();
    status.setStatus("");

    Set<ConstraintViolation<ApplicationStatus>> violations = validator.validate(status);

    //@NotBlankと@Patternの2種類
    assertThat(violations).hasSize(2);
    assertThat(violations).anyMatch(v ->
        v.getPropertyPath().toString().equals("status"));
  }
}