package dto.response.teacher;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherHomeroomDetailResponse {
    private Long id;
    private String name;
    private String teacherName;
    private Long teacherId;
    private String teacherEmail;
    private String teacherAvatarUrl;
    private int totalStudents;
    private int anticipatedStudents;
    private int expelledStudents;
    private int graduatedStudents;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<TeacherHomeroomStudentResponse> students;
}