package dto.request.admin;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchRemoveStudentsRequest {
    
    @NotNull(message = "Homeroom ID is required")
    private Long homeroomId;
    
    @NotEmpty(message = "Student IDs cannot be empty")
    private List<Long> studentIds;
}