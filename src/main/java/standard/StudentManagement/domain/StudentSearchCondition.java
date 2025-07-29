package standard.StudentManagement.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentSearchCondition {

  @Size(max = 100)
  private String name;

  @Email
  private String email;

  @Size(max = 100)
  private String area;

  private boolean deleted;

  @Min(0)
  @Max(150)
  private int minAge;

  @Min(0)
  @Max(150)
  private int maxAge;

  @Pattern(regexp = "男性|女性|その他")
  private String sex;

  @Size(max = 100)
  private String courseName;

  @Pattern(regexp = "仮申込|本申込|受講中|受講終了")
  private String status;
}
