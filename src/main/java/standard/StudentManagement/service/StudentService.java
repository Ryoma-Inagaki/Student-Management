package standard.StudentManagement.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import standard.StudentManagement.controller.converter.StudentConverter;
import standard.StudentManagement.data.Student;
import standard.StudentManagement.data.StudentCourse;
import standard.StudentManagement.domain.StudentDetail;
import standard.StudentManagement.repository.StudentRepository;

/**
 * 受講生情報を取り扱うサービスです。 受講生の検索や登録・更新処理を行います。
 */
@Service
public class StudentService {

  private StudentRepository repository;
  private StudentConverter converter;

  @Autowired
  public StudentService(StudentRepository repository, StudentConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  /**
   * 受講生一覧検索機能です。 全件検索を行うので、条件指定は行いません。
   *
   * @return 受講生一覧(全件)
   */
  public List<StudentDetail> getStudentList() {
    List<Student> studentList = repository.searchStudent();
    List<StudentCourse> studentCourseList = repository.searchStudentCourseList();
    return converter.convertStudentDetails(studentList, studentCourseList);
  }

  /**
   * 受講生検索です。 IDに紐づく受講生情報を取得した後、その受講生に紐づく受講生コース情報を取得して設定します。
   *
   * @param id 　受講生ID
   * @return 受講生
   */
  public StudentDetail getStudentProfile(String id) {
    Student student = repository.searchStudentById(id);
    List<StudentCourse> studentCourses = repository.searchStudentCourseListByStudentId(
        student.getId());
    return new StudentDetail(student, studentCourses);
  }

  /**
   * 受講生情報および関連する受講コース情報を登録します。
   *
   * @param studentDetail 登録する受講生詳細情報
   * @return 登録済みの受講生詳細情報
   */
  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();

    assignStudentId(student);
    repository.registerStudent(student);

    registerCourseWithStudentId(studentDetail);

    return studentDetail;
  }

  /**
   * 受講生コース情報に受講生IDを設定し、開始日と終了日を登録します。
   *
   * @param studentDetail 受講生詳細情報
   */
  private void registerCourseWithStudentId(StudentDetail studentDetail) {
    String studentId = studentDetail.getStudent().getId();
    LocalDateTime now = LocalDateTime.now();

    for (StudentCourse studentCourse : studentDetail.getStudentCourseList()) {
      studentCourse.setStudentId(studentId);
      studentCourse.setStartAt(now);
      studentCourse.setEndAt(now.plusMonths(6));
      repository.registerStudentCourseList(studentCourse);
    }
  }

  /**
   * UUIDを用いて受講生IDを採番します。
   *
   * @param student 受講生
   */
  private void assignStudentId(Student student) {
    student.setId(UUID.randomUUID().toString());
  }

  /**
   * 受講生情報、関連する受講生コース情報、またはその両方を更新します。
   *
   * @param studentDetail 更新対象の受講生詳細情報
   */
  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());
    for (StudentCourse studentCourse : studentDetail.getStudentCourseList()) {
      repository.updateStudentCourseList(studentCourse);
    }
  }
}
