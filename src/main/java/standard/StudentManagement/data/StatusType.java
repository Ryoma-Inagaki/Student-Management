package standard.StudentManagement.data;

/**
 * 申込状況の種別を表す列挙型です。
 * 各ステータスには対応する整数IDがあります。
 */
public enum StatusType {
  仮申込(1),
  本申込(2),
  受講中(3),
  受講終了(4);

  private int id;

  StatusType(int id) {
    this.id = id;
  }

  /**
   * このステータスに対応する数値IDを返します。
   *
   * @return ステータスに対応するID（1〜4）
   */
  public int getId() {
    return id;
  }

  /**
   * IDからステータスを取得します。
   *
   * @param id ステータスID（例: 1〜4）
   * @return 対応する {@link StatusType}、該当しない場合は {@code null}
   */
  public static StatusType fromId(int id) {
    for (StatusType type : values()) {
      if (type.id == id) {
        return type;
      }
    }
    return null;
  }

  /**
   * ステータス名（文字列）からIDを取得します。
   *
   * @param name ステータス名（例: "仮申込"）
   * @return 対応する数値ID
   * @throws IllegalArgumentException 不正なステータス名が指定された場合
   */
  public static int toId(String name) {
    for (StatusType type : values()) {
      if (type.name().equals(name)) {
        return type.id;
      }
    }
    throw new IllegalArgumentException("不正なステータス: " + name);
  }
}
