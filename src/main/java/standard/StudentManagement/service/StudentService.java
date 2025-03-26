package standard.StudentManagement.service;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import standard.StudentManagement.data.Student;
import standard.StudentManagement.data.StudentCourse;
import standard.StudentManagement.domain.StudentDetail;
import standard.StudentManagement.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> getStudentList() {
    return repository.searchStudent();
  }

  public List<StudentCourse> getStudentCourseList() {
    return repository.searchStudentCourses();
  }

  private void assignStudentId(Student student) {
    student.setId(UUID.randomUUID().toString());
  }

  public void registerStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();
    assignStudentId(student);
    repository.registerStudent(student);
  }
}
