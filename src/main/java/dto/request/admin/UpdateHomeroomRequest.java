package dto.request.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateHomeroomRequest {
    
    @NotNull(message = "Homeroom ID is required")
    private Long id;
    
    @NotBlank(message = "Homeroom name cannot be empty")
    private String name;

    private Long teacherId;
}