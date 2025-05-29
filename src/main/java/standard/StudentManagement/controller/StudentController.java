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
import standard.StudentManagement.domain.StudentDetail;
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


}
