package dto.response.student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.HomeroomStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentHomeroomDetailResponse {
    private Long id;
    private String name;
    private Long teacherId;
    private String teacherName;
    private String teacherEmail;
    private String teacherAvatarUrl;
    private HomeroomStatus status;
    private int anticipatedStudents;
    private int expelledStudents;
    private int graduatedStudents;
    private String statusName;
    private int totalStudents;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<StudentHomeroomClassmateResponse> classmates;
}