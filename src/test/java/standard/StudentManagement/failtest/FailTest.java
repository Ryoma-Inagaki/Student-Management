package standard.StudentManagement.failtest;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

public class FailTest {

  @Test
  void intentionalFail() {
    fail("このテストはわざと失敗します");
  }
}
