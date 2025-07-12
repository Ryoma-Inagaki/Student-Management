package standard.StudentManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "申込状況")
@Getter
@Setter
public class ApplicationStatus {

  private String id;
  private int studentCourseId;

  @Schema(description = "申込状況の状態", example = "仮申込", allowableValues = {"仮申込", "本申込", "受講中", "受講終了"})
  @NotBlank(message = "申込状況は必須です。")
  @Pattern(regexp = "仮申込|本申込|受講中|受講終了",message = "申込状況は「仮申込」「本申込」「受講中」「受講終了」のいずれかで指定してください。")
  private String status;

}
