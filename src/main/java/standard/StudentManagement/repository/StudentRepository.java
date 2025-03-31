package standard.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import standard.StudentManagement.data.Student;
import standard.StudentManagement.data.StudentCourse;


@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students")
  List<Student> searchStudent();

  @Select("SELECT * FROM students_courses")
  List<StudentCourse> searchStudentCourses();

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

}
