package standard.StudentManagement.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 受講生の検索条件を表すクラスです。
 * 名前やメールアドレス、地域、年齢、コース名、申込状況などの条件を指定して検索します。
 */
@Schema(description = "受講生の検索条件")
@Getter
@Setter
public class StudentSearchCondition {

  @Size(max = 100, message = "名前は100文字以内で入力してください。")
  private String name;

  @Email(message = "正しいメールアドレス形式で入力してください。")
  private String email;

  @Size(max = 100, message = "地域は100文字以内で入力してください。")
  private String area;

  private boolean deleted;

  @Min(value = 0, message = "年齢は0以上を指定してください。")
  @Max(value = 150, message = "年齢は150以下を指定してください。")
  private int minAge;

  @Min(value = 0, message = "年齢は0以上を指定してください。")
  @Max(value = 150, message = "年齢は150以下を指定してください。")
  private int maxAge;

  private String sex;

  @Size(max = 100, message = "コース名は100文字以内で入力してください。")
  private String courseName;

  private String status;
}
