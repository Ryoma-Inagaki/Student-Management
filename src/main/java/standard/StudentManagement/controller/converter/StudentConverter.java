package standard.StudentManagement.controller.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import standard.StudentManagement.data.ApplicationStatus;
import standard.StudentManagement.data.StatusType;
import standard.StudentManagement.data.Student;
import standard.StudentManagement.data.StudentCourse;
import standard.StudentManagement.domain.StudentDetail;

/**
 * 受講生詳細を受講生や受講生コース情報、もしくはその逆の変換を行うコンバーターです。
 * 各受講生に紐づく受講生コース情報をマッピングし、さらにコースに紐づく申込状況(ApplicationStatus)も補完します。
 */
@Component
public class StudentConverter {

  /**
   * 受講生に紐づく受講生コース情報をマッピングする。
   * 受講生コース情報は受講生に対して複数存在するのでループを回して受講生詳細情報を組み立てる。
   * コースに申込状況が存在する場合は、statusId から status（文字列）を補完して整合性を保ちます。
   *
   * @param studentList　受講生一覧
   * @param studentCourseList　受講生コース情報のリスト(申込状況を含む)
   * @return 受講生詳細情報のリスト
   */
  public List<StudentDetail> convertStudentDetails(List<Student> studentList,
      List<StudentCourse> studentCourseList) {
    List<StudentDetail> studentDetails = new ArrayList<>();
    studentList.forEach(student -> {
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student);

      List<StudentCourse> convertStudentCourseList = studentCourseList.stream()
          .filter(studentCourse -> student.getId().equals(studentCourse.getStudentId()))
          .peek(course -> {
            ApplicationStatus status = course.getApplicationStatus();
            if (status != null
                && status.getStatus() == null
                && StatusType.fromId(status.getStatusId()) != null) {
              status.setStatusId(status.getStatusId());
            }
          })
          .collect(Collectors.toList());

      studentDetail.setStudentCourseList(convertStudentCourseList);
      studentDetails.add(studentDetail);
    });
    return studentDetails;
  }
}
