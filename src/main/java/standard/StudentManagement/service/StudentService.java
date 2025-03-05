package standard.StudentManagement.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import standard.StudentManagement.data.Student;
import standard.StudentManagement.data.StudentCourse;
import standard.StudentManagement.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> getStudentList() {
    return repository.search();
  }

  public List<StudentCourse> getStudentCourseList() {
    return repository.searchStudentCourses();
  }
}
