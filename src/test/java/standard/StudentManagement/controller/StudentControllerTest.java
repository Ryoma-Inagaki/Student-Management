package standard.StudentManagement.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import standard.StudentManagement.data.ApplicationStatus;
import standard.StudentManagement.data.Student;
import standard.StudentManagement.data.StudentCourse;
import standard.StudentManagement.domain.StudentDetail;
import standard.StudentManagement.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private StudentService service;

  @Test
  void getStudentList_受講生詳細の一覧検索が実行でき空のリストが返ってくること()
      throws Exception {
    when(service.getStudentList()).thenReturn(Collections.emptyList());

    mockMvc.perform(get("/studentList"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    verify(service, times(1)).getStudentList();
  }

  @Test
  void getStudent_指定したIDの受講生情報が取得できること() throws Exception {
    String id = "test123";

    StudentDetail testStudentDetail = getTestStudentDetail();

    when(service.getStudentProfile(id)).thenReturn(testStudentDetail);

    String json = objectMapper.writeValueAsString(testStudentDetail);

    mockMvc.perform(get("/student/{id}", id))
        .andExpect(status().isOk())
        .andExpect(content().json(json));

    verify(service, times(1)).getStudentProfile(id);
  }

  @Test
  void registerStudent_リクエストを送信すると受講生情報が登録されて返されること() throws Exception {
    StudentDetail testStudentDetail = getTestStudentDetail();

    when(service.registerStudent(any(StudentDetail.class))).thenReturn(testStudentDetail);

    String json = objectMapper.writeValueAsString(testStudentDetail);

    mockMvc.perform(post("/registerStudent")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andExpect(content().json(json));

    verify(service, times(1)).registerStudent(any(StudentDetail.class));
  }

  @Test
  void updateStudent_更新リクエストを送信すると成功メッセージが返ること() throws Exception {
    StudentDetail testStudentDetail = getTestStudentDetail();

    doNothing().when(service).updateStudent(any(StudentDetail.class));

    String json = objectMapper.writeValueAsString(testStudentDetail);

    mockMvc.perform(put("/updateStudent")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andExpect(content().string("更新処理が成功しました。"));

    verify(service, times(1)).updateStudent(any(StudentDetail.class));
  }

  public StudentDetail getTestStudentDetail() {
    ApplicationStatus status = new ApplicationStatus();
    // @Pattern の制約に合致する必要あるため"仮申込"
    status.setStatus("仮申込");

    Student testStudent = new Student();
    testStudent.setId("test123");
    testStudent.setName("山田テスト");
    testStudent.setKanaName("ヤマダテスト");
    testStudent.setNickname("テストマン");
    testStudent.setEmail("yamada@example.com");
    testStudent.setArea("東京");
    testStudent.setAge(20);
    testStudent.setSex("男性");

    StudentCourse course1 = new StudentCourse();
    course1.setId(1);
    course1.setStudentId(testStudent.getId());
    course1.setCourseName("Java入門");
    course1.setStartAt(LocalDateTime.of(2025, 6, 1, 9, 0));
    course1.setEndAt(LocalDateTime.of(2025, 6, 30, 18, 0));
    course1.setApplicationStatus(status);

    StudentCourse course2 = new StudentCourse();
    course2.setId(2);
    course2.setStudentId(testStudent.getId());
    course2.setCourseName("Spring基礎");
    course2.setStartAt(LocalDateTime.of(2025, 7, 1, 9, 0));
    course2.setEndAt(LocalDateTime.of(2025, 7, 31, 18, 0));
    course2.setApplicationStatus(status);
    return new StudentDetail(testStudent, List.of(course1, course2));
  }

  @Test
  void getTestException_例外がスローされエラーメッセージが返ること() throws Exception {
    mockMvc.perform(get("/students"))
        .andExpect(status().isBadRequest())
        .andExpect(
            content().string("現在このAPIは利用できません。URLは｢studentList｣を利用してください。"));
  }
}
