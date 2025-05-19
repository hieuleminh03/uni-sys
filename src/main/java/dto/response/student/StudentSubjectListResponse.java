package dto.response.student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentSubjectListResponse {
    
    private Long id;
    private String name;
    private String code;
    private String description;
    private int totalClasses;
    private int enrolledClasses;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}