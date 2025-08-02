package standard.StudentManagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import standard.StudentManagement.data.Student;
import standard.StudentManagement.domain.StudentDetail;
import standard.StudentManagement.domain.StudentSearchCondition;
import standard.StudentManagement.exception.ErrorResponse;
import standard.StudentManagement.exception.TestException;
import standard.StudentManagement.service.StudentService;

/**
 * 受講生の検索や登録、更新などを行うREST APIとして受け付けるControllerです。
 */
@Tag(name = "受講生管理API", description = "受講生の検索・登録・更新などの操作を提供します。")
@Validated
@RestController
public class StudentController {

  private StudentService service;

  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  /**
   * 受講生一覧検索機能です。 全件検索を行うので、条件指定は行いません。
   *
   * @return 受講生一覧(全件)
   */
  @Operation(summary = "一覧検索", description = "受講生の一覧を検索します。")
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() {
    return service.getStudentList();
  }

  /**
   * 受講生検索機能です。 IDに紐づく任意の受講生の情報を取得します。
   *
   * @param id 受講生ID
   * @return 受講生
   */
  @Operation(summary = "受講生情報取得", description = "指定されたIDの受講生情報を取得します。")
  @GetMapping("/student/{id}")
  public StudentDetail getStudent(@PathVariable String id) {
    return service.getStudentProfile(id);
  }

  /**
   * 受講生登録機能です。
   *
   * @param studentDetail 登録する受講生詳細情報
   * @return 登録された受講生詳細情報
   */
  @Operation(summary = "受講生登録", description = "受講生を登録します。",
      responses = {
          @ApiResponse(responseCode = "200", description = "登録に成功しました",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = StudentDetail.class))),
          @ApiResponse(responseCode = "400", description = "入力値が不正です",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class))),
          @ApiResponse(responseCode = "500", description = "サーバーエラー",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class)))
      }
  )
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(
      @Parameter(description = "登録する受講生情報")
      @RequestBody @Valid StudentDetail studentDetail) {
    StudentDetail responseStudentDetail = service.registerStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }

  /**
   * 受講生更新機能です。
   *
   * @param studentDetail 更新する受講生詳細情報
   * @return 更新処理の結果メッセージ
   */

  @Operation(summary = "受講生更新", description = "受講生情報を更新します。",
      responses = {
          @ApiResponse(responseCode = "200", description = "更新に成功しました",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = String.class))),
          @ApiResponse(responseCode = "400", description = "入力値が不正です",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class))),
          @ApiResponse(responseCode = "500", description = "サーバーエラー",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class)))
      }
  )
  @PutMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(
      @Parameter(description = "更新する受講生情報")
      @RequestBody @Valid StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました。");
  }

  @Operation(hidden = true)
  @GetMapping("/students")
  public String getTestException() throws TestException {
    throw new TestException("現在このAPIは利用できません。URLは｢studentList｣を利用してください。");
  }

  /**
   * 受講生の検索条件を指定して検索を行います。
   *
   * @param condition 検索条件（名前、メールアドレス、地域、性別、年齢範囲、コース名、申込状況、削除フラグ）
   * @return 条件に一致した受講生一覧
   */
  @Operation(summary = "条件付き受講生検索", description = "指定された検索条件に一致する受講生を取得します。",
      responses = {
          @ApiResponse(responseCode = "200", description = "検索結果の受講生一覧",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = Student.class))),
          @ApiResponse(responseCode = "400", description = "検索条件が不正です",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class))),
          @ApiResponse(responseCode = "500", description = "サーバーエラー",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class)))
      }
  )
  @PostMapping("/searchStudents")
  public ResponseEntity<List<Student>> searchStudents(
      @RequestBody @Valid StudentSearchCondition condition) {

    List<Student> students = service.searchStudentByCondition(condition);
    return ResponseEntity.ok(students);
  }
}
