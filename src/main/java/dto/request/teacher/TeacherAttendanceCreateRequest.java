package dto.request.teacher;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import model.enums.AttendanceStatus;
import model.enums.AttendanceType;

import java.util.Date;
import java.util.List;

@Getter
public class TeacherAttendanceCreateRequest {
    @NotBlank(message = "Class ID cannot be blank")
    private Long classId;

    @NotBlank(message = "Attendance type cannot be blank")
    private AttendanceType attendanceType;

    @NotBlank(message = "Date cannot be blank")
    private Date date;

    @NotBlank(message = "Attendance results cannot be blank")
    private List<AttendanceResult> attendanceResults;

    @Getter
    public static class AttendanceResult {
        @NotBlank(message = "Class student ID cannot be blank")
        private Long classStudentId;
        @NotBlank(message = "Student name cannot be blank")
        private AttendanceStatus attendanceStatus;
    }
}
