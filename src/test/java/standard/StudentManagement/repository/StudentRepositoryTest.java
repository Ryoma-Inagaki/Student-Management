package standard.StudentManagement.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import standard.StudentManagement.data.ApplicationStatus;
import standard.StudentManagement.data.StatusType;
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
  void searchStudentCourseList_申込状況がマッピングされていること() {
    List<StudentCourse> courseList = sut.searchStudentCourseList();

    StudentCourse course = courseList.stream()
        .filter(c -> c.getApplicationStatus() != null)
        .findFirst()
        .orElseThrow(() -> new AssertionError("申込状況がnullでマッピングされていません"));

    ApplicationStatus status = course.getApplicationStatus();
    assertThat(status).isNotNull();
    assertThat(status.getStatusId()).isEqualTo(1);
    assertThat(status.getStatus()).isEqualTo("仮申込");
  }

  @Test
  void searchStudentCourseList_申込状況が存在しない場合はnullになること() {
    StudentCourse course = new StudentCourse();
    course.setStudentId("11111111-1111-1111-1111-111111111111");
    course.setCourseName("テストコース");
    course.setStartAt(LocalDateTime.now());
    course.setEndAt(LocalDateTime.now().plusMonths(1));

    sut.registerStudentCourseList(course);

    List<StudentCourse> courseList = sut.searchStudentCourseList();
    StudentCourse target = courseList.stream()
        .filter(c -> c.getCourseName().equals("テストコース"))
        .findFirst()
        .orElseThrow();

    assertThat(target.getApplicationStatus()).isNull();
  }

  @Test
  void registerApplicationStatus_存在しないstudentCourseIdを指定すると例外が出ること() {
    ApplicationStatus status = new ApplicationStatus();
    status.setId(UUID.randomUUID().toString());
    status.setStudentCourseId(999999);
    status.setStatus("仮申込");
    status.setStatusId(1);

    assertThatThrownBy(() -> sut.registerApplicationStatus(status))
        .isInstanceOf(DataIntegrityViolationException.class);
  }


  @Test
  void searchStudentCourseListByStudentId_受講生IDに紐づく受講生コース情報の検索が行えること() {
    List<StudentCourse> actual = sut.searchStudentCourseListByStudentId(
        "11111111-1111-1111-1111-111111111111");
    assertThat(actual).hasSize(2);
    assertThat(actual.get(0).getCourseName()).isEqualTo("Java入門");
  }

  @Test
  void searchApplicationStatusByStudentCourseId_受講生コースIDに紐づく申込状況を取得できること() {
    ApplicationStatus actual = sut.searchApplicationStatusByStudentCourseId(1);
    assertThat(actual).isNotNull();
    assertThat(actual.getStatusType()).isEqualTo(StatusType.仮申込);
  }

  @Test
  void searchApplicationStatusByStudentCourseId_存在しないIDはnullを返すこと() {
    ApplicationStatus actual = sut.searchApplicationStatusByStudentCourseId(999);
    assertThat(actual).isNull();
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
        .isInstanceOf(DataIntegrityViolationException.class);
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
        .isInstanceOf(DuplicateKeyException.class);
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
  void registerApplicationStatus_申込状況の登録が行えること() {
    StudentCourse course = new StudentCourse();
    course.setStudentId("11111111-1111-1111-1111-111111111111");
    course.setCourseName("JUnit実践");
    course.setStartAt(Timestamp.valueOf("2025-07-01 09:00:00").toLocalDateTime());
    course.setEndAt(Timestamp.valueOf("2025-07-31 18:00:00").toLocalDateTime());

    sut.registerStudentCourseList(course);

    ApplicationStatus status = new ApplicationStatus();
    status.setId(UUID.randomUUID().toString());
    status.setStudentCourseId(course.getId());
    status.setStatusType(StatusType.仮申込);

    sut.registerApplicationStatus(status);

    ApplicationStatus actual = sut.searchApplicationStatusByStudentCourseId(course.getId());
    assertThat(actual).isNotNull();
    assertThat(actual.getStatusType()).isEqualTo(StatusType.仮申込);
  }

  @Test
  void registerApplicationStatus_存在しない受講生コースIdを指定した場合例外が出ること() {
    ApplicationStatus status = new ApplicationStatus();
    status.setId(UUID.randomUUID().toString());
    status.setStudentCourseId(999);
    status.setStatusType(StatusType.仮申込);

    assertThatThrownBy(() -> sut.registerApplicationStatus(status))
        .isInstanceOf(DataIntegrityViolationException.class);
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
        .isInstanceOf(DataIntegrityViolationException.class);
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

  @Test
  void updateApplicationStatus_申込状況が更新されること() {
    ApplicationStatus status = sut.searchApplicationStatusByStudentCourseId(1);

    status.setStatusType(StatusType.受講中);
    sut.updateApplicationStatus(status);

    ApplicationStatus result = sut.searchApplicationStatusByStudentCourseId(1);
    assertThat(result.getStatusType()).isEqualTo(StatusType.受講中);
  }

  @Test
  void updateApplicationStatus_存在しない受講生コースIDを指定しても影響がないこと() {
    ApplicationStatus status = new ApplicationStatus();
    status.setId(UUID.randomUUID().toString());
    status.setStudentCourseId(9999);
    status.setStatusType(StatusType.仮申込);

    sut.updateApplicationStatus(status);

    ApplicationStatus result = sut.searchApplicationStatusByStudentCourseId(9999);
    assertThat(result).isNull();
  }
}
