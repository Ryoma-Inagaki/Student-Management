package standard.StudentManagement;


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


//  リストで取るにはどうする？
  @GetMapping("/student")
  public String getStudent(@RequestParam String name) {
    Student student = repository.searchByName(name);
    return student.getName() + " " + student.getAge() + "歳" + " ID " + student.getId();
  }

  @PostMapping("/student")
  public void registerStudent(String name, int age, String id) {
    repository.registerStudent(name, age, id);
  }

  @PatchMapping("/student")
  public void updateStudentId(@RequestParam String name,@RequestParam String id) {
    repository.updateStudentId(name, id);
  }

  @DeleteMapping("/student")
  public void deleteStudent(String name) {
    repository.deleteStudent(name);
  }
}
