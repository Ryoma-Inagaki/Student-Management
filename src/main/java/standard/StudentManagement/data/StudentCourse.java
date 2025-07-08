package standard.StudentManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生コース情報")
@Getter
@Setter
public class StudentCourse {

  private int id;

  @NotBlank(message = "受講生IDは必須です。")
  private String studentId;

  @NotBlank(message = "コース名は必須です。")
  private String courseName;

  @NotNull(message = "開始日は必須です。")
  private LocalDateTime startAt;

  @NotNull(message = "終了日は必須です。")
  private LocalDateTime endAt;

}
