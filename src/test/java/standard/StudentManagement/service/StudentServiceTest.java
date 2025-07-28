package standard.StudentManagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import standard.StudentManagement.controller.converter.StudentConverter;
import standard.StudentManagement.data.ApplicationStatus;
import standard.StudentManagement.data.Student;
import standard.StudentManagement.data.StudentCourse;
import standard.StudentManagement.domain.StudentDetail;
import standard.StudentManagement.domain.StudentSearchCondition;
import standard.StudentManagement.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  private StudentService sut;
  private Clock clock;

  private Student testStudent;
  private List<StudentCourse> testCourseList;
  private StudentDetail testStudentDetail;


  @BeforeEach
  void setUp() {
    clock = Clock.systemDefaultZone();
    sut = new StudentService(repository, converter, clock);

    testStudent = new Student();
    testStudent.setId("test123");
    testStudent.setName("山田テストマン");
    testStudent.setNickname("テストマン");

    StudentCourse course1 = new StudentCourse();
    ApplicationStatus status1 = new ApplicationStatus();
    status1.setStatus("仮申込");
    course1.setApplicationStatus(status1);

    StudentCourse course2 = new StudentCourse();
    ApplicationStatus status2 = new ApplicationStatus();
    status2.setStatus("受講中");
    course2.setApplicationStatus(status2);

    testCourseList = List.of(course1, course2);

    testStudentDetail = new StudentDetail(testStudent, testCourseList);
  }

  @Test
  void getStudentList_リポジトリとコンバーターの処理が適切に呼び出せていること() {
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    when(repository.searchStudent()).thenReturn(studentList);
    when(repository.searchStudentCourseList()).thenReturn(studentCourseList);

    sut.getStudentList();

    verify(repository, times(1)).searchStudent();
    verify(repository, times(1)).searchStudentCourseList();
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList);
  }

  @Test
  void getStudentProfile_リポジトリの処理が適切に呼び出せていること() {
    String studentId = "test123";

    Student mockStudent = new Student();
    mockStudent.setId(studentId);
    mockStudent.setName("山田テスト");

    StudentCourse course = new StudentCourse();
    course.setId(1);
    course.setStudentId(studentId);

    ApplicationStatus status = new ApplicationStatus();
    status.setStatus("仮申込");

    List<StudentCourse> mockCourses = List.of(course);

    when(repository.searchStudentById(studentId)).thenReturn(mockStudent);
    when(repository.searchStudentCourseListByStudentId(studentId)).thenReturn(mockCourses);
    when(repository.searchApplicationStatusByStudentCourseId(1)).thenReturn(status);

    StudentDetail result = sut.getStudentProfile(studentId);

    verify(repository, times(1)).searchStudentById(studentId);
    verify(repository, times(1)).searchStudentCourseListByStudentId(studentId);
    verify(repository, times(1)).searchApplicationStatusByStudentCourseId(1);

    assertEquals(studentId, result.getStudent().getId());
    assertEquals("山田テスト", result.getStudent().getName());
    assertEquals("仮申込", result.getStudentCourseList().get(0).getApplicationStatus().getStatus());
  }

  @Test
  void registerStudent_リポジトリの処理が適切によびだせていること_IDがnullにならないこと() {
    sut.registerStudent(testStudentDetail);

    assertNotNull(testStudent.getId());
    verify(repository).registerStudent(testStudent);
    verify(repository, times(2)).registerStudentCourseList(any(StudentCourse.class));
  }

  @Test
  void registerCourseAndStatusWithStudentId_コース情報に学生IDと日付が正しく設定され申込状況も登録されること() {
    LocalDateTime fixedDateTime = LocalDateTime.of(2025, 6, 12, 0, 0);
    Clock fixedClock = Clock.fixed(fixedDateTime.atZone(ZoneId.systemDefault()).toInstant(),
        ZoneId.systemDefault());

    sut = new StudentService(repository, converter, fixedClock);

    for (StudentCourse studentCourse : testCourseList) {
      ApplicationStatus status = new ApplicationStatus();
      status.setStatus("仮申込");
      studentCourse.setApplicationStatus(status);
    }

    sut.registerCourseAndStatusWithStudentId(testStudentDetail);

    for (StudentCourse studentCourse : testCourseList) {
      assertEquals("test123", studentCourse.getStudentId());
      assertEquals(fixedDateTime, studentCourse.getStartAt());
      assertEquals(fixedDateTime.plusMonths(6), studentCourse.getEndAt());
    }
    verify(repository, times(2)).registerStudentCourseList(any(StudentCourse.class));
    verify(repository, times(2)).registerApplicationStatus(any(ApplicationStatus.class));
  }

  @Test
  void registerApplicationStatusForCourse_申込状況がある場合登録されること() {
    StudentCourse course = new StudentCourse();
    ApplicationStatus status = new ApplicationStatus();
    course.setApplicationStatus(status);

    sut.registerApplicationStatusForCourse(course);

    verify(repository, times(1)).registerApplicationStatus(status);
  }

  @Test
  void registerApplicationStatusForCourse_申込状況がnullの場合登録されないこと() {
    StudentCourse course = new StudentCourse();

    sut.registerApplicationStatusForCourse(course);

    verify(repository, never()).registerApplicationStatus(any());
  }

  @Test
  void updateStudent_リポジトリの処理が適切によびだせていること() {
    for (StudentCourse course : testCourseList) {
      ApplicationStatus status = new ApplicationStatus();
      status.setStatus("仮申込");
      course.setApplicationStatus(status);
    }

    sut.updateStudent(testStudentDetail);

    verify(repository).updateStudent(testStudent);
    verify(repository, times(2)).updateStudentCourseList(any(StudentCourse.class));
    verify(repository, times(2)).updateApplicationStatus(any(ApplicationStatus.class));
  }

  @Test
  void updateStudent_申込状況がnullの場合は更新されないこと() {
    for (StudentCourse course : testCourseList) {
      course.setApplicationStatus(null);
    }

    sut.updateStudent(testStudentDetail);

    verify(repository).updateStudent(testStudent);
    verify(repository, times(2)).updateStudentCourseList(any(StudentCourse.class));
    verify(repository, never()).updateApplicationStatus(any());
  }

  @Test
  void searchStudentByCondition_リポジトリの処理が適切に呼び出せていること() {
    StudentSearchCondition condition = new StudentSearchCondition();
    condition.setName("山本");

    List<Student> mockResult = new ArrayList<>();
    Student student = new Student();
    student.setName("山本テスト");
    mockResult.add(student);

    when(repository.searchStudentByCondition(condition)).thenReturn(mockResult);

    List<Student> result = sut.searchStudentByCondition(condition);

    verify(repository, times(1)).searchStudentByCondition(condition);
    assertEquals(1, result.size());
    assertEquals("山本テスト", result.get(0).getName());
  }

  @Test
  void searchStudentByCondition_検索条件がすべて空の場合_全件検索になること() {
    StudentSearchCondition condition = new StudentSearchCondition();

    List<Student> mockResult = new ArrayList<>();
    Student student1 = new Student(); student1.setName("山本太郎");
    Student student2 = new Student(); student2.setName("鈴木花子");
    mockResult.add(student1);
    mockResult.add(student2);

    when(repository.searchStudentByCondition(condition)).thenReturn(mockResult);

    List<Student> result = sut.searchStudentByCondition(condition);

    verify(repository, times(1)).searchStudentByCondition(condition);
    assertEquals(2, result.size());
    assertEquals("山本太郎", result.get(0).getName());
    assertEquals("鈴木花子", result.get(1).getName());
  }
}
