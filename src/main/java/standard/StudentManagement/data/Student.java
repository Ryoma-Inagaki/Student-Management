package standard.StudentManagement.data;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Validated
@Getter
@Setter
public class Student {

  private String id;
  private String name;
  private String kanaName;
  private String nickname;
  private String email;
  private String area;
  @Min(value = 15)
  private int age;
  private String sex;
  private String remark;
  private boolean deleted;
}
