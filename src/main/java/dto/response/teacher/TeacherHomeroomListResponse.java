package dto.response.teacher;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherHomeroomListResponse {
    private Long id;
    private String name;
    private int studentCount;
    private int anticipatedStudents;
    private int expelledStudents;
    private int graduatedStudents;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}