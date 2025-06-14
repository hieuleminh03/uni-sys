package dto.response.teacher;

import lombok.Builder;
import lombok.Getter;
import model.enums.AttendanceStatus;
import model.enums.AttendanceType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Builder
@Getter
public class AttendanceListResponse {

    private Long classStudentId;
    private String studentName;
    private String studentEmail;

    private List<AttendanceHistoriesResponse> attendanceHistories;

    @Builder
    @Getter
    public static class AttendanceHistoriesResponse {
        private Long attendanceId;
        private LocalDateTime date;
        private AttendanceType attendanceType;
        private AttendanceStatus attendanceStatus;
    }
}
