package standard.StudentManagement.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import standard.StudentManagement.data.Student;
import standard.StudentManagement.data.StudentCourse;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void searchStudent_受講生情報の全件検索が行えること() {
    List<Student> actual = sut.searchStudent();
    assertThat(actual.size()).isEqualTo(5);
  }

  @Test
  void searchStudent_isDeletedがtrueの受講生は含まれないこと() {
    Student student = sut.searchStudentById("11111111-1111-1111-1111-111111111111");
    student.setDeleted(true);
    sut.updateStudent(student);

    List<Student> students = sut.searchStudent();

    boolean contains = students.stream().anyMatch(s -> s.getId().equals(student.getId()));
    assertThat(contains).isFalse();
  }

  @Test
  void searchStudentById_IDから受講生情報が取得できること() {
    Student actual = sut.searchStudentById("11111111-1111-1111-1111-111111111111");
    assertThat(actual).isNotNull();
    assertThat(actual.getName()).isEqualTo("山田太郎");
    assertThat(actual.getEmail()).isEqualTo("yamada@example.com");
  }

  @Test
  void searchStudentById_存在しないIDはnullを返すこと() {
    Student student = sut.searchStudentById("99999999-9999-9999-9999-999999999999");
    assertThat(student).isNull();
  }

  @Test
  void searchStudentCourseList_受講生コースの全件検索が行えること() {
    List<StudentCourse> actual = sut.searchStudentCourseList();
    assertThat(actual).hasSize(5);
  }

  @Test
  void searchStudentCourseListByStudentId_受講生IDに紐づく受講生コース情報の検索が行えること() {
    List<StudentCourse> actual = sut.searchStudentCourseListByStudentId(
        "11111111-1111-1111-1111-111111111111");
    assertThat(actual).hasSize(2);
    assertThat(actual.get(0).getCourseName()).isEqualTo("Java入門");
  }

  @Test
  void registerStudent_受講生情報の登録が行えること() {
    String id = UUID.randomUUID().toString();
    Student student = new Student();
    student.setId(id);
    student.setName("山本テスト");
    student.setKanaName("ヤマモトテスト");
    student.setNickname("テストマン");
    student.setEmail("yamamoto@example.com");
    student.setArea("東京");
    student.setAge(20);
    student.setSex("男性");
    student.setRemark("");
    student.setDeleted(false);

    sut.registerStudent(student);

    List<Student> actual = sut.searchStudent();
    assertThat(actual.size()).isEqualTo(6);
  }

  @Test
  void registerStudent_必須項目がnullの場合に例外がスローされること() {
    Student student = new Student();
    student.setId(UUID.randomUUID().toString());
    student.setEmail("test@example.com");

    assertThatThrownBy(() -> sut.registerStudent(student))
        .isInstanceOf(org.springframework.dao.DataIntegrityViolationException.class);
  }

  @Test
  void registerStudent_メールが重複している場合に例外がスローされること() {
    Student student = new Student();
    student.setId(UUID.randomUUID().toString());
    student.setName("山本テスト");
    student.setKanaName("山本テスト");
    student.setEmail("yamada@example.com");
    student.setDeleted(false);

    assertThatThrownBy(() -> sut.registerStudent(student))
        .isInstanceOf(org.springframework.dao.DuplicateKeyException.class);
  }

  @Test
  void registerStudentCourseList_受講生コース情報の登録が行えること() {
    StudentCourse course = new StudentCourse();
    course.setStudentId("11111111-1111-1111-1111-111111111111");
    course.setCourseName("JUnit実践");
    course.setStartAt(Timestamp.valueOf("2025-07-01 09:00:00").toLocalDateTime());
    course.setEndAt(Timestamp.valueOf("2025-07-31 18:00:00").toLocalDateTime());

    sut.registerStudentCourseList(course);

    List<StudentCourse> actual = sut.searchStudentCourseListByStudentId(
        "11111111-1111-1111-1111-111111111111");
    assertThat(actual).hasSize(3);
  }

  @Test
  void updateStudent_受講生情報が更新されること() {
    Student student = sut.searchStudentById("11111111-1111-1111-1111-111111111111");
    student.setName("名前更新テスト");
    student.setDeleted(true);

    sut.updateStudent(student);

    Student update = sut.searchStudentById("11111111-1111-1111-1111-111111111111");
    assertThat(update.getName()).isEqualTo("名前更新テスト");
    assertThat(update.isDeleted()).isTrue();
  }

  @Test
  void updateStudent_存在しないIDを更新しても影響がないこと() {
    Student student = new Student();
    student.setId("non-existent-id");
    student.setName("ダミー");
    student.setKanaName("ダミー");
    student.setEmail("dummy@example.com");
    student.setDeleted(false);

    sut.updateStudent(student);

    Student result = sut.searchStudentById("non-existent-id");
    assertThat(result).isNull();
  }

  @Test
  void updateStudentCourseList_受講生コース名が更新されること() {
    List<StudentCourse> courseList = sut.searchStudentCourseListByStudentId(
        "11111111-1111-1111-1111-111111111111");
    StudentCourse course = courseList.get(0);
    int id = course.getId();

    course.setCourseName("テスト入門");

    sut.updateStudentCourseList(course);

    List<StudentCourse> updateList = sut.searchStudentCourseListByStudentId(
        "11111111-1111-1111-1111-111111111111");
    StudentCourse updated = updateList.stream()
        .filter(c -> c.getId() == id)
        .findFirst()
        .orElseThrow();

    assertThat(updated.getCourseName()).isEqualTo("テスト入門");
  }

  @Test
  void registerStudentCourseList_存在しない受講生IDを指定した場合に例外が出ること() {
    StudentCourse course = new StudentCourse();
    course.setStudentId("non-existent-id");
    course.setCourseName("架空のコース");
    course.setStartAt(Timestamp.valueOf("2025-07-01 09:00:00").toLocalDateTime());
    course.setEndAt(Timestamp.valueOf("2025-07-31 18:00:00").toLocalDateTime());

    assertThatThrownBy(() -> sut.registerStudentCourseList(course))
        .isInstanceOf(org.springframework.dao.DataIntegrityViolationException.class);
  }

  @Test
  void updateStudentCourseList_存在しないIDを指定しても影響がないこと() {
    StudentCourse course = new StudentCourse();
    course.setId(9999);
    course.setStudentId("11111111-1111-1111-1111-111111111111");
    course.setCourseName("存在しないIDで更新");
    course.setStartAt(Timestamp.valueOf("2025-07-01 09:00:00").toLocalDateTime());
    course.setEndAt(Timestamp.valueOf("2025-07-31 18:00:00").toLocalDateTime());

    sut.updateStudentCourseList(course);

    List<StudentCourse> courseList = sut.searchStudentCourseListByStudentId(
        "11111111-1111-1111-1111-111111111111");
    boolean updated = courseList.stream()
        .anyMatch(c -> c.getCourseName().equals("存在しないIDで更新"));
    assertThat(updated).isFalse();
  }
}
