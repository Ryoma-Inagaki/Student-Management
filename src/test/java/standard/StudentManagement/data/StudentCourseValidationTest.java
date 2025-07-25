package standard.StudentManagement.data;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentCourseValidationTest {

  private Validator validator;

  @BeforeEach
  void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void 正常な入力値の場合はバリデーションエラーにならない() {
    StudentCourse course = createValidCourse();

    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(course);

    assertThat(violations).isEmpty();
  }

  @Test
  void 必須項目が未入力の場合はバリデーションエラーになる() {
    StudentCourse course = new StudentCourse(); // 何も設定しない

    ApplicationStatus status = new ApplicationStatus();
    course.setApplicationStatus(status);

    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(course);

    assertThat(violations).hasSize(5);
    assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("studentId"));
    assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("courseName"));
    assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("startAt"));
    assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("endAt"));
    assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("applicationStatus.status"));
  }

  private StudentCourse createValidCourse() {
    ApplicationStatus status = new ApplicationStatus();
    // @Pattern の制約に合致する必要あるため"仮申込"
    status.setStatus("仮申込");

    StudentCourse course = new StudentCourse();
    course.setId(1);
    course.setStudentId("test123");
    course.setCourseName("Spring基礎");
    course.setStartAt(LocalDateTime.of(2025, 6, 1, 9, 0));
    course.setEndAt(LocalDateTime.of(2025, 6, 30, 18, 0));
    course.setApplicationStatus(status);
    return course;
  }
}