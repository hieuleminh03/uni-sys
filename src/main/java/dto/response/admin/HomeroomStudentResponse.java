package dto.response.admin;

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
public class HomeroomStudentResponse {
    
    private Long id;
    private Long studentId;
    private String studentName;
    private String studentCode;
    private String studentAvatarUrl;
    private String studentEmail;
    private HomeroomStatus status;
    private String statusName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}