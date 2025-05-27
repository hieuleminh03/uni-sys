package dto.response.admin;

import lombok.Builder;
import lombok.Getter;
import model.enums.ExaminationType;

import java.util.Date;
import java.util.List;

@Getter
@Builder
public class ExamListResponse {
    private Long examinationId;
    private Date date;
    private ExaminationType examinationType;
    private List<ExamStudentResponse> examStudentResponses;
    private List<AttendanceResponse> attendanceResponses;

    @Builder
    @Getter
    public static class ExamStudentResponse {
        private Long studentExaminationId;
        private String studentName;
        private String studentEmail;
        private Double grade;
        private boolean isAbsent;
    }

    @Builder
    @Getter
    public static class AttendanceResponse {
        private Long classStudentId;
        private String studentName;
        private String studentEmail;
        private Integer absenceAttendanceCount;
        private Integer totalAttendanceCount;
    }
}
