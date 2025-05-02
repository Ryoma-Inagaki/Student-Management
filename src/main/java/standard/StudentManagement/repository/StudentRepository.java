package standard.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import standard.StudentManagement.data.Student;
import standard.StudentManagement.data.StudentCourse;


@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students WHERE is_deleted = false")
  List<Student> searchStudent();

  @Select("SELECT * FROM students WHERE id = #{id}")
  Student searchStudentById(String id);

  @Select("SELECT * FROM students_courses")
  List<StudentCourse> searchStudentCourses();

  @Select("SELECT * FROM students_courses WHERE student_id = #{studentId}")
  List<StudentCourse> searchStudentCoursesByStudentId(String studentId);


  @Insert("""
      INSERT INTO students (id, name, kana_name, nickname, email, area, age, sex, remark, is_deleted)
      VALUES (#{id}, #{name}, #{kanaName}, #{nickname}, #{email}, #{area}, #{age}, #{sex}, #{remark}, false)
      """)
  void registerStudent(Student student);

  @Insert("""
      INSERT INTO students_courses (student_id, course_name, start_at, end_at)
      VALUES (#{studentId}, #{courseName}, #{startAt}, #{endAt})
      """)
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void registerStudentCourses(StudentCourse studentCourse);

  @Update("""
      UPDATE students SET name = #{name}, kana_name = #{kanaName}, nickname = #{nickname},
      email = #{email}, area = #{area}, age = #{age}, sex = #{sex}, remark = #{remark}, is_deleted = #{deleted}
      WHERE id = #{id}
      """)
  void updateStudent(Student student);

  @Update("""
       UPDATE students_courses SET course_name = #{courseName}
      WHERE id = #{id}
      """)
  void updateStudentCourses(StudentCourse studentCourse);

}
