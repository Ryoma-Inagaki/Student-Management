package standard.StudentManagement;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController

public class StudentManagementApplication {

  @Autowired
  private StudentRepository repository;

  public static void main(String[] args) {
    SpringApplication.run(StudentManagementApplication.class, args);
  }


  @GetMapping("/student")
  public Student getStudent(@RequestParam String name) {
    return repository.searchByName(name);
  }

  @GetMapping("/allStudents")
  public List<Student> getAllStudents() {
    List<Student> students = repository.findAll();
    return students;
  }

  @PostMapping("/student")
  public void registerStudent(String name, int age, String id) {
    repository.registerStudent(name, age, id);
  }

  @PatchMapping("/student")
  public void updateStudentAge(String name, int age) {
    repository.updateStudentAge(name, age);
  }

  @DeleteMapping("/student")
  public void deleteStudent(String name) {
    repository.deleteStudent(name);
  }
}
