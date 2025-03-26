package standard.StudentManagement.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import standard.StudentManagement.controller.converter.StudentConverter;
import standard.StudentManagement.data.Student;
import standard.StudentManagement.data.StudentCourse;
import standard.StudentManagement.domain.StudentDetail;
import standard.StudentManagement.service.StudentService;

@Controller
public class StudentController {

  private StudentService service;
  private StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  @GetMapping("/studentList")
  public String getStudentList(Model model) {
    List<Student> students = service.getStudentList();
    List<StudentCourse> studentCourses = service.getStudentCourseList();

    model.addAttribute("studentList", converter.convertStudentDetails(students, studentCourses));
    return "studentList";
  }

  @GetMapping("/studentCourseList")
  public List<StudentCourse> getStudentCourseList() {
    return service.getStudentCourseList();
  }

  @GetMapping("/newStudent")
  public String newStudent(Model model) {
    model.addAttribute("studentDetail",  new StudentDetail());
    return "registerStudent";
  }

  @PostMapping("/registerStudent")
  public String registerStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if (result.hasErrors()) {
      return "registerStudent";
    }
    Student newStudent = studentDetail.getStudent();
    service.registerStudent(newStudent);

    return "redirect:/studentList";
  }
}
