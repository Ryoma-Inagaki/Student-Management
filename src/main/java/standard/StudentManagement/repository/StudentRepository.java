package standard.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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
  @Select("SELECT * FROM students WHERE is_deleted = false")
  List<Student> searchStudent();

  /**
   * 受講生の検索を行います。
   *
   * @param id 受講生ID
   * @return 受講生
   */
  @Select("SELECT * FROM students WHERE id = #{id}")
  Student searchStudentById(String id);

  /**
   * 受講生のコース情報の全件検索を行います。
   *
   * @return 受講生のコース情報(全件)
   */
  @Select("SELECT * FROM students_courses")
  List<StudentCourse> searchStudentCourses();

  /**
   * 受講生IDに紐づく受講生コース情報の検索を行います。
   *
   * @param studentId 受講生ID
   * @return 受講生IDに紐づく受講生コース情報
   */
  @Select("SELECT * FROM students_courses WHERE student_id = #{studentId}")
  List<StudentCourse> searchStudentCoursesByStudentId(String studentId);


  /**
   * 受講生情報を登録します。
   *
   * @param student 登録する受講生情報
   */
  @Insert("""
      INSERT INTO students (id, name, kana_name, nickname, email, area, age, sex, remark, is_deleted)
      VALUES (#{id}, #{name}, #{kanaName}, #{nickname}, #{email}, #{area}, #{age}, #{sex}, #{remark}, false)
      """)
  void registerStudent(Student student);

  /**
   * 受講生コース情報を登録します。
   * 主キー(ID)は自動採番されます。
   *
   * @param studentCourse 登録する受講生コース情報
   */
  @Insert("""
      INSERT INTO students_courses (student_id, course_name, start_at, end_at)
      VALUES (#{studentId}, #{courseName}, #{startAt}, #{endAt})
      """)
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void registerStudentCourses(StudentCourse studentCourse);

  /**
   * 受講生情報を更新します。
   * 指定されたIDに一致する受講生レコードの情報を、引数の内容で上書きします。
   *
   * @param student 更新する受講生情報(IDは必須)
   */
  @Update("""
      UPDATE students SET name = #{name}, kana_name = #{kanaName}, nickname = #{nickname},
      email = #{email}, area = #{area}, age = #{age}, sex = #{sex}, remark = #{remark}, is_deleted = #{deleted}
      WHERE id = #{id}
      """)
  void updateStudent(Student student);

  /**
   * 受講生コース情報を更新します。
   * 指定されたIDに一致する受講コースの名称を変更します。
   *
   * @param studentCourse 更新する受講生コース情報(IDとcourseNameは必須)
   */
  @Update("""
       UPDATE students_courses SET course_name = #{courseName}
      WHERE id = #{id}
      """)
  void updateStudentCourses(StudentCourse studentCourse);

}
