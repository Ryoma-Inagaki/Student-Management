package standard.StudentManagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
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
import standard.StudentManagement.data.Student;
import standard.StudentManagement.data.StudentCourse;
import standard.StudentManagement.domain.StudentDetail;
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
    StudentCourse course2 = new StudentCourse();
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

    List<StudentCourse> mockCourses = new ArrayList<>();
    StudentCourse course = new StudentCourse();
    course.setStudentId(studentId);
    mockCourses.add(course);

    when(repository.searchStudentById(studentId)).thenReturn(mockStudent);
    when(repository.searchStudentCourseListByStudentId(studentId)).thenReturn(mockCourses);

    StudentDetail result = sut.getStudentProfile(studentId);

    verify(repository, times(1)).searchStudentById(studentId);
    verify(repository, times(1)).searchStudentCourseListByStudentId(studentId);

    assertEquals(studentId, result.getStudent().getId());
    assertEquals("山田テスト", result.getStudent().getName());

  }

  @Test
  void registerStudent_リポジトリの処理が適切によびだせていること_IDがnullにならないこと() {
    sut.registerStudent(testStudentDetail);

    assertNotNull(testStudent.getId());
    verify(repository).registerStudent(testStudent);
    verify(repository, times(2)).registerStudentCourseList(any(StudentCourse.class));
  }

  @Test
  void registerCourseWithStudentId_コース情報に学生IDと日付が正しく設定されること() {
    LocalDateTime fixedDateTime = LocalDateTime.of(2025, 6, 12, 0, 0);
    Clock fixedClock = Clock.fixed(fixedDateTime.atZone(ZoneId.systemDefault()).toInstant(),
        ZoneId.systemDefault());

    sut = new StudentService(repository, converter, fixedClock);

    sut.registerCourseWithStudentId(testStudentDetail);

    for (StudentCourse studentCourse : testCourseList) {
      assertEquals("test123", studentCourse.getStudentId());
      assertEquals(fixedDateTime, studentCourse.getStartAt());
      assertEquals(fixedDateTime.plusMonths(6), studentCourse.getEndAt());
    }
    verify(repository, times(2)).registerStudentCourseList(any(StudentCourse.class));
  }

  @Test
  void updateStudent_リポジトリの処理が適切によびだせていること() {
    sut.updateStudent(testStudentDetail);

    verify(repository).updateStudent(testStudent);
    verify(repository, times(2)).updateStudentCourseList(any(StudentCourse.class));
  }
}
