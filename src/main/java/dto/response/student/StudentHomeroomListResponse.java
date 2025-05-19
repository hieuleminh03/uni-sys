package dto.response.student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.HomeroomStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentHomeroomListResponse {
    private Long id;
    private String name;
    private Long teacherId;
    private String teacherName;
    private HomeroomStatus status;
    private String statusName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}