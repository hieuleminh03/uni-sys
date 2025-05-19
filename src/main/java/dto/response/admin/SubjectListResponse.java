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
public class SubjectListResponse {
    
    private Long id;
    private String name;
    private String code;
    private String description;
    private int totalClasses;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}