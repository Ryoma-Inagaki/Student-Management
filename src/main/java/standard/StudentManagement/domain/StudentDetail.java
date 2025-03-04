package standard.StudentManagement.domain;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import standard.StudentManagement.data.Student;
import standard.StudentManagement.data.StudentCourse;

@Getter
@Setter
public class StudentDetail {

  private Student student;
  private List<StudentCourse> studentCourses;

}
