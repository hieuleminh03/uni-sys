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
public class UpdateHomeroomTeacherRequest {
    
    @NotNull(message = "Homeroom ID is required")
    private Long homeroomId;
    
    // If teacherId is null, it means the teacher should be removed
    private Long teacherId;
}