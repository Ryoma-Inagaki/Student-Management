package standard.StudentManagement.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import standard.StudentManagement.controller.converter.StudentConverter;
import standard.StudentManagement.data.Student;
import standard.StudentManagement.data.StudentCourse;
import standard.StudentManagement.domain.StudentDetail;
import standard.StudentManagement.service.StudentService;

@RestController
public class StudentController {

  private StudentService service;
  private StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() {
    List<Student> students = service.getStudentList();
    List<StudentCourse> studentCourses = service.getStudentCourseList();

    return converter.convertStudentDetails(students, studentCourses);
  }

  @GetMapping("/studentCourseList")
  public List<StudentCourse> getStudentCourseList() {
    return service.getStudentCourseList();
  }
}
