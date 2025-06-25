package standard.StudentManagement.data;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentValidationTest {

  private Validator validator;

  @BeforeEach
  void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void 入力値がすべて正しい場合はバリデーションエラーにならない() {
    Student student = createValidStudent();

    Set<ConstraintViolation<Student>> violations = validator.validate(student);
    assertThat(violations).isEmpty();
  }

  @Test
  void 必須項目が未入力の場合はバリデーションエラーになる() {
    Student student = new Student(); // 何もセットしない

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations).hasSize(6);
    assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("kanaName"));
    assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("area"));
    assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("sex"));
    assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("age"));
  }

  @Test
  void 年齢が14歳以下だとバリデーションエラーになる() {
    Student student = createValidStudent();
    student.setAge(14);

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations).hasSize(1);
    assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("age");
  }

  @Test
  void メールアドレスが不正な形式だとバリデーションエラーになる() {
    Student student = createValidStudent();
    student.setEmail("not-an-email");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations).hasSize(1);
    assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("email");
  }

  private Student createValidStudent() {
    Student student = new Student();
    student.setId("test123");
    student.setName("山田テスト");
    student.setKanaName("ヤマダテスト");
    student.setNickname("テストマン");
    student.setEmail("yamada@example.com");
    student.setArea("東京");
    student.setAge(20);
    student.setSex("男性");
    return student;
  }
}
