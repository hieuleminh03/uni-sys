package dto.request.admin;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RemoveStudentFromHomeroomRequest {
    
    @NotNull(message = "Homeroom ID is required")
    private Long homeroomId;
    
    @NotNull(message = "Student ID is required")
    private Long studentId;
}