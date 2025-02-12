package standard.StudentManagement;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM student WHERE name = #{name}")
  Student searchByName(String name);

  @Insert("INSERT student values(#{name}, #{age}, #{id})")
  void registerStudent(String name, int age, String id);

  @Update("UPDATE student SET id = #{id} WHERE name = #{name}")
  void updateStudentId(String name, String id);

  @Delete("DELETE FROM student WHERE name = #{name}")
  void deleteStudent(String name);
}
