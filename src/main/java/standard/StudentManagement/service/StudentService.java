package standard.StudentManagement.service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import standard.StudentManagement.controller.converter.StudentConverter;
import standard.StudentManagement.data.ApplicationStatus;
import standard.StudentManagement.data.Student;
import standard.StudentManagement.data.StudentCourse;
import standard.StudentManagement.domain.StudentDetail;
import standard.StudentManagement.domain.StudentSearchCondition;
import standard.StudentManagement.repository.StudentRepository;

/**
 * 受講生情報を取り扱うサービスです。 受講生の検索や登録・更新処理を行います。
 */
@Service
public class StudentService {

  private StudentRepository repository;
  private StudentConverter converter;
  private Clock clock;

  @Autowired
  public StudentService(StudentRepository repository, StudentConverter converter) {
    this(repository, converter, Clock.systemDefaultZone());
  }

  /**
   * テスト用途で使用するコンストラクタです。 Clock を任意に注入可能にすることで、日時に依存するロジックの検証を容易にします。
   *
   * @param repository 受講生リポジトリ
   * @param converter  受講生コンバータ
   * @param clock      テスト用の Clock インスタンス
   */
  StudentService(StudentRepository repository, StudentConverter converter, Clock clock) {
    this.repository = repository;
    this.converter = converter;
    this.clock = clock;
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
   * 受講生検索です。 指定されたIDに紐づく受講生情報を取得し、その受講生に紐づく受講生コース情報と 各コースに紐づく申込状況を取得して設定します。
   *
   * @param id 　受講生ID
   * @return 受講生詳細情報(コース情報および申込状況を含む)
   */
  public StudentDetail getStudentProfile(String id) {
    Student student = repository.searchStudentById(id);
    List<StudentCourse> studentCourses = repository.searchStudentCourseListByStudentId(
        student.getId());

    studentCourses.forEach(course -> {
      ApplicationStatus status = repository.searchApplicationStatusByStudentCourseId(
          course.getId());
      course.setApplicationStatus(status);
    });

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

    registerCourseAndStatusWithStudentId(studentDetail);

    return studentDetail;
  }

  /**
   * 受講生コース情報に受講生IDを設定し、開始日と終了日を登録します。 また、各コースに紐づく申込状況が存在する場合は、それも同時に登録します。
   *
   * @param studentDetail 受講生詳細情報
   */

  void registerCourseAndStatusWithStudentId(StudentDetail studentDetail) {
    String studentId = studentDetail.getStudent().getId();
    LocalDateTime now = LocalDateTime.now(clock);

    for (StudentCourse studentCourse : studentDetail.getStudentCourseList()) {
      studentCourse.setStudentId(studentId);
      studentCourse.setStartAt(now);
      studentCourse.setEndAt(now.plusMonths(6));
      repository.registerStudentCourseList(studentCourse);

      registerApplicationStatusForCourse(studentCourse);
    }
  }

  /**
   * 指定された受講生コースに紐づく申込状況を登録します。 申込状況がnullの場合は何もせずスキップされます。
   *
   * @param studentCourse 申込状況を持つ受講生コース
   */
  void registerApplicationStatusForCourse(StudentCourse studentCourse) {
    ApplicationStatus status = studentCourse.getApplicationStatus();

    if (status != null) {
      status.setId(UUID.randomUUID().toString());
      status.setStudentCourseId(studentCourse.getId());
      repository.registerApplicationStatus(status);
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
   * 受講生情報を更新します。関連する受講生コース情報および申込状況も同時に更新します。
   *
   * @param studentDetail 更新対象の受講生詳細情報
   */
  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());
    updateCourseAndStatus(studentDetail);
  }

  /**
   * 受講生詳細情報に含まれる受講生コース情報を更新し、 それに紐づく申込状況も存在すれば更新します。
   *
   * @param studentDetail　更新対象の受講生詳細情報(複数の受講生コースと申込状況を含む)
   */
  void updateCourseAndStatus(StudentDetail studentDetail) {
    for (StudentCourse studentCourse : studentDetail.getStudentCourseList()) {
      repository.updateStudentCourseList(studentCourse);
      updateApplicationStatusIfPresent(studentCourse);
    }
  }

  /**
   * 受講生コースに申込状況が設定されていれば、それを更新します。 申込状況がnullの場合はスキップされます。
   *
   * @param studentCourse 　更新対象の受講生コース(申込状況を含む可能性あり)
   */
  void updateApplicationStatusIfPresent(StudentCourse studentCourse) {
    ApplicationStatus status = studentCourse.getApplicationStatus();
    if (status != null) {
      repository.updateApplicationStatus(status);
    }
  }

  /**
   * 指定された検索条件に基づいて受講生を検索します。
   * 最小年齢が最大年齢を上回っている場合は {@link IllegalArgumentException} をスローします。
   *
   * @param condition 検索条件（名前、メールアドレス、地域、性別、年齢範囲、コース名、申込状況、削除フラグ）
   * @return 条件に一致する受講生のリスト
   * @throws IllegalArgumentException 最小年齢が最大年齢を上回っている場合
   */
  public List<Student> searchStudentByCondition(StudentSearchCondition condition) {
    if (condition.getMinAge() > condition.getMaxAge()) {
      throw new IllegalArgumentException("最小年齢は最大年齢以下にしてください");
    }
    return repository.searchStudentByCondition(condition);
  }
}
