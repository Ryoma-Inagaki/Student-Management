package standard.StudentManagement;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.qos.logback.core.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
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

}
