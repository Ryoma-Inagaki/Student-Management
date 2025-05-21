package standard.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import standard.StudentManagement.data.Student;
import standard.StudentManagement.data.StudentCourse;

/**
 * 受講生テーブルと受講生コース情報テーブルと紐づくリポジトリです。
 */
@Mapper
public interface StudentRepository {

  /**
   * 受講生の全件検索を行います。
   *
   * @return 受講生一覧(全件)
   */
  List<Student> searchStudent();

  /**
   * 受講生の検索を行います。
   *
   * @param id 受講生ID
   * @return 受講生
   */
  Student searchStudentById(String id);

  /**
   * 受講生のコース情報の全件検索を行います。
   *
   * @return 受講生のコース情報(全件)
   */
  List<StudentCourse> searchStudentCourseList();

  /**
   * 受講生IDに紐づく受講生コース情報の検索を行います。
   *
   * @param studentId 受講生ID
   * @return 受講生IDに紐づく受講生コース情報
   */
  List<StudentCourse> searchStudentCourseListByStudentId(String studentId);

  /**
   * 受講生情報を登録します。
   *
   * @param student 登録する受講生情報
   */
  void registerStudent(Student student);

  /**
   * 受講生コース情報を登録します。 主キー(ID)は自動採番されます。
   *
   * @param studentCourse 登録する受講生コース情報
   */
  void registerStudentCourseList(StudentCourse studentCourse);

  /**
   * 受講生情報を更新します。 指定されたIDに一致する受講生レコードの情報を、引数の内容で上書きします。
   *
   * @param student 更新する受講生情報(IDは必須)
   */
  void updateStudent(Student student);

  /**
   * 受講生コース情報を更新します。 指定されたIDに一致する受講コースの名称を変更します。
   *
   * @param studentCourse 更新する受講生コース情報(IDとcourseNameは必須)
   */
  void updateStudentCourseList(StudentCourse studentCourse);

}
