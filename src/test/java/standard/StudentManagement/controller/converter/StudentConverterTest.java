package standard.StudentManagement.controller.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import standard.StudentManagement.data.Student;
import standard.StudentManagement.data.StudentCourse;
import standard.StudentManagement.domain.StudentDetail;

class StudentConverterTest {

  private StudentConverter converter = new StudentConverter();

  @Test
  void convertStudentDetails_受講生と対応するコースが正しくマッピングされる() {
    Student student1 = new Student();
    student1.setId("name1");
    student1.setName("山田太郎");

    Student student2 = new Student();
    student2.setId("name2");
    student2.setName("佐藤花子");

    StudentCourse course1 = new StudentCourse();
    course1.setId(1);
    course1.setStudentId("name1");
    course1.setCourseName("AWSコース");

    StudentCourse course2 = new StudentCourse();
    course2.setId(2);
    course2.setStudentId("name2");
    course2.setCourseName("Javaベーシック");

    StudentCourse course3 = new StudentCourse();
    course3.setId(3);
    course3.setStudentId("name2");
    course3.setCourseName("Javaスタンダード");

    List<Student> studentList = Arrays.asList(student1, student2);
    List<StudentCourse> studentCourseList = Arrays.asList(course1, course2, course3);

    List<StudentDetail> testStudentDetails = converter.convertStudentDetails(studentList,
        studentCourseList);

    assertThat(testStudentDetails).hasSize(2);

    StudentDetail detail1 = testStudentDetails.get(0);
    assertThat(detail1.getStudent().getId()).isEqualTo("name1");
    assertThat(detail1.getStudentCourseList()).hasSize(1);
    assertThat(detail1.getStudentCourseList().get(0).getCourseName()).isEqualTo("AWSコース");

    StudentDetail detail2 = testStudentDetails.get(1);
    assertThat(detail2.getStudent().getId()).isEqualTo("name2");
    assertThat(detail2.getStudentCourseList()).hasSize(2);
    assertThat(detail2.getStudentCourseList())
        .extracting(StudentCourse::getCourseName)
        .containsExactlyInAnyOrder("Javaベーシック", "Javaスタンダード");
  }

  @Test
  void convertStudentDetails_コースが存在しない受講生でも空のリストが返ってくる() {
    Student student = new Student();
    student.setId("test123");
    student.setName("山本テスト");

    List<Student> studentList = List.of(student);
    List<StudentCourse> studentCourseList = List.of();

    List<StudentDetail> testStudentDetails = converter.convertStudentDetails(studentList,
        studentCourseList);

    assertThat(testStudentDetails).hasSize(1);
    assertThat(testStudentDetails.get(0).getStudent().getId()).isEqualTo("test123");
    assertThat(testStudentDetails.get(0).getStudentCourseList()).isEmpty();
  }

}