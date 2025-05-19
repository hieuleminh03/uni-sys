package dto.response.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomeroomListResponse {
    
    private Long id;
    private String name;
    private Long teacherId;
    private String teacherName;
    private int studentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}