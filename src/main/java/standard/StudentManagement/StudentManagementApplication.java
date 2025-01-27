package standard.StudentManagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController

public class StudentManagementApplication {

  private List<Map<String, String>> studentList = new ArrayList<>();

  public StudentManagementApplication() {
//    new HashMap<>();
//  student1.put("name","Ryoma Inagaki");
//  student1.put("age","32");
//  studentList.add(student1);
//  }
  }

  public static void main(String[] args) {
    SpringApplication.run(StudentManagementApplication.class, args);
  }

  @GetMapping("/studentInfo")
  public List<Map<String, String>> getStudentInfo() {
    return studentList;
  }

  @PostMapping("/addStudentInfo")
  public String setStudentInfo(@RequestParam String name, @RequestParam String age) {
    Map<String, String> newStudent = new HashMap<>();
    newStudent.put("name", name);
    newStudent.put("age", age);
    studentList.add(newStudent);
    return newStudent.toString();
  }

//  @PostMapping("/studentName")
//  public void updateStudentName(String name) {
//    this.name = name;

}
