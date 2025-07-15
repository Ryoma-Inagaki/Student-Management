package standard.StudentManagement.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class ApplicationStatusTest {

  @Test
  void setStatusType_EnumからstatusとstatusIdが正しく設定されること() {
    ApplicationStatus applicationStatus = new ApplicationStatus();
    applicationStatus.setStatusType(StatusType.本申込);

    assertThat(applicationStatus.getStatus()).isEqualTo("本申込");
    assertThat(applicationStatus.getStatusId()).isEqualTo(2);
  }

  @Test
  void getStatusType_statusからEnumに正しく変換できること() {
    ApplicationStatus applicationStatus = new ApplicationStatus();
    applicationStatus.setStatus("受講中");

    StatusType type = applicationStatus.getStatusType();

    assertThat(type).isEqualTo(StatusType.受講中);
  }

  @Test
  void getStatusType_不正なstatusの場合に例外がスローされること() {
    ApplicationStatus applicationStatus = new ApplicationStatus();
    applicationStatus.setStatus("不正な状態");

    assertThatThrownBy(applicationStatus::getStatusType)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("No enum constant");
  }

  @Test
  void getStatusTypeById_statusIdからEnumに正しく変換できること() {
    ApplicationStatus applicationStatus = new ApplicationStatus();
    applicationStatus.setStatusId(3);

    StatusType type = applicationStatus.getStatusTypeById();

    assertThat(type).isEqualTo(StatusType.受講中);
  }

  @Test
  void getStatusTypeById_不正なstatusIdの場合はnullを返すこと() {
    ApplicationStatus applicationStatus = new ApplicationStatus();
    applicationStatus.setStatusId(999);

    StatusType type = applicationStatus.getStatusTypeById();

    assertThat(type).isNull();
  }
}
