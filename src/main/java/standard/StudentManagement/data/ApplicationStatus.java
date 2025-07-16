package standard.StudentManagement.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

  @Schema(description = "申込状況のID", example = "1", allowableValues = {"1", "2", "3", "4"})
  private int statusId;

  @Schema(description = "申込状況の状態", example = "仮申込", allowableValues = {"仮申込", "本申込", "受講中", "受講終了"})
  @NotBlank(message = "申込状況は必須です。")
  @Pattern(regexp = "仮申込|本申込|受講中|受講終了", message = "申込状況は「仮申込」「本申込」「受講中」「受講終了」のいずれかで指定してください。")
  private String status;

  /**
   * StatusType（列挙型）を設定します。
   * このメソッドを使うと {@code status} と {@code statusId} の両方が同期して設定されます。
   *
   * @param statusType 設定する申込状況（列挙型）
   */
  public void setStatusType(StatusType statusType) {
    this.status = statusType.name();
    this.statusId = statusType.getId();
  }

  /**
   * 現在の {@code status} の文字列から StatusType を返します。
   *
   * @return 対応する StatusType（列挙型）
   * @throws IllegalArgumentException {@code status} が不正な場合にスローされます
   */
  public StatusType getStatusType() {
    return StatusType.valueOf(this.status);
  }

  /**
   * 現在の {@code statusId} から StatusType を取得します。
   * 該当する ID がない場合は {@code null} を返します。
   *
   * @return StatusType または {@code null}
   */
  @JsonIgnore
  public StatusType getStatusTypeById() {
    return StatusType.fromId(this.statusId);
  }

  /**
   * 文字列のステータスを設定し、それに対応する {@code statusId} も自動で設定します。
   * 不正な値の場合、{@code statusId} は {@code 0} に設定されます
   *
   * @param status 　ステータスの文字列（例: "仮申込"）
   */
  public void setStatus(String status) {
    this.status = status;
    try {
      this.statusId = StatusType.toId(status);
    } catch (IllegalArgumentException e) {
      this.statusId = 0;
    }
  }

  /**
   * ステータスIDを設定し、それに対応する {@code status} の文字列も自動で設定します。
   *
   * @param statusId ステータスID（例: 1）
   */
  public void setStatusId(int statusId) {
    this.statusId = statusId;
    StatusType type = StatusType.fromId(statusId);
    if (type != null) {
      this.status = type.name();
    }
  }
}
