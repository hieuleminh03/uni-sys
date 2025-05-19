package dto.request.admin;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.HomeroomStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddStudentToHomeroomRequest {
    
    @NotNull(message = "Homeroom ID is required")
    private Long homeroomId;
    
    @NotNull(message = "Student ID is required")
    private Long studentId;
    
    @NotNull(message = "Status is required")
    private HomeroomStatus status;
}