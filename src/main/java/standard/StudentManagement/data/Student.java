package standard.StudentManagement.data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Student {

  private String id;

  @NotBlank(message = "名前は必須です。")
  private String name;

  @NotBlank(message = "カナ名は必須です。")
  private String kanaName;

  private String nickname;

  @NotBlank(message = "メールアドレスは必須です。")
  @Email(message = "正しいメールアドレス形式で入力してください。")
  private String email;

  @NotBlank(message = "地域は必須です。")
  private String area;

  @Min(value = 15, message = "年齢は15歳以上で入力してください。")
  private int age;

  @NotBlank(message = "性別は必須です。")
  private String sex;

  private String remark;

  private boolean deleted;
}
