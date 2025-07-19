package standard.StudentManagement.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class StatusTypeTest {

  @Test
  void fromId_既存のIDを指定すると対応するStatusTypeが返される() {
    assertThat(StatusType.fromId(1)).isEqualTo(StatusType.仮申込);
    assertThat(StatusType.fromId(2)).isEqualTo(StatusType.本申込);
    assertThat(StatusType.fromId(3)).isEqualTo(StatusType.受講中);
    assertThat(StatusType.fromId(4)).isEqualTo(StatusType.受講終了);
  }

  @Test
  void fromId_存在しないIDを指定するとnullが返される() {
    assertThat(StatusType.fromId(999)).isNull();
    assertThat(StatusType.fromId(-1)).isNull();
  }

  @Test
  void toId_既存の名前を指定すると対応するIDが返される() {
    assertThat(StatusType.toId("仮申込")).isEqualTo(1);
    assertThat(StatusType.toId("本申込")).isEqualTo(2);
    assertThat(StatusType.toId("受講中")).isEqualTo(3);
    assertThat(StatusType.toId("受講終了")).isEqualTo(4);
  }

  @Test
  void toId_存在しない名前を指定すると例外がスローされる() {
    assertThatThrownBy(() -> StatusType.toId("未登録"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("不正なステータス");

    assertThatThrownBy(() -> StatusType.toId(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("不正なステータス");
  }
}
