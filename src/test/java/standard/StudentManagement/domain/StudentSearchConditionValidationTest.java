package standard.StudentManagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentSearchConditionValidationTest {

  private Validator validator;

  @BeforeEach
  void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void 正常な入力ではバリデーションエラーが発生しないこと() {
    StudentSearchCondition condition = new StudentSearchCondition();
    condition.setName("山田太郎");
    condition.setEmail("test@example.com");
    condition.setArea("東京");
    condition.setMinAge(20);
    condition.setMaxAge(30);
    condition.setSex("男性");
    condition.setCourseName("Java入門");
    condition.setStatus("仮申込");

    Set<ConstraintViolation<StudentSearchCondition>> violations = validator.validate(condition);

    assertThat(violations).isEmpty();
  }

  @Test
  void nameが101文字以上だとバリデーションエラーになること() {
    StudentSearchCondition condition = new StudentSearchCondition();
    condition.setName("a".repeat(101));

    Set<ConstraintViolation<StudentSearchCondition>> violations = validator.validate(condition);

    assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
  }

  @Test
  void emailが不正な形式ならバリデーションエラーになること() {
    StudentSearchCondition condition = new StudentSearchCondition();
    condition.setEmail("not-an-email");

    Set<ConstraintViolation<StudentSearchCondition>> violations = validator.validate(condition);

    assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
  }

  @Test
  void areaが101文字以上だとバリデーションエラーになること() {
    StudentSearchCondition condition = new StudentSearchCondition();
    condition.setArea("a".repeat(101));

    Set<ConstraintViolation<StudentSearchCondition>> violations = validator.validate(condition);

    assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("area"));
  }

  @Test
  void courseNameが101文字以上だとバリデーションエラーになること() {
    StudentSearchCondition condition = new StudentSearchCondition();
    condition.setCourseName("a".repeat(101));

    Set<ConstraintViolation<StudentSearchCondition>> violations = validator.validate(condition);

    assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("courseName"));
  }

  @Test
  void minAgeがマイナスだとバリデーションエラーになること() {
    StudentSearchCondition condition = new StudentSearchCondition();
    condition.setMinAge(-1);

    Set<ConstraintViolation<StudentSearchCondition>> violations = validator.validate(condition);

    assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("minAge"));
  }

  @Test
  void maxAgeがマイナスだとバリデーションエラーになること() {
    StudentSearchCondition condition = new StudentSearchCondition();
    condition.setMaxAge(-1);

    Set<ConstraintViolation<StudentSearchCondition>> violations = validator.validate(condition);

    assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("maxAge"));
  }

  @Test
  void minAgeが150を超えるとバリデーションエラーになること() {
    StudentSearchCondition condition = new StudentSearchCondition();
    condition.setMinAge(151);

    Set<ConstraintViolation<StudentSearchCondition>> violations = validator.validate(condition);

    assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("minAge"));
  }

  @Test
  void maxAgeが150を超えるとバリデーションエラーになること() {
    StudentSearchCondition condition = new StudentSearchCondition();
    condition.setMaxAge(151);

    Set<ConstraintViolation<StudentSearchCondition>> violations = validator.validate(condition);

    assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("maxAge"));
  }

}
