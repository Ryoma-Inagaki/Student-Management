package standard.StudentManagement.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentSearchCondition {

  private String name;

  private String email;

  private String area;

  private boolean deleted;

  private int minAge;

  private int maxAge;

  private String sex;

  private String courseName;

  private String status;
}
