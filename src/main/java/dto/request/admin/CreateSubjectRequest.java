package dto.request.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSubjectRequest {
    
    @NotBlank(message = "Subject name cannot be empty")
    private String name;
    
    @NotBlank(message = "Subject code cannot be empty")
    @Pattern(regexp = "^[A-Z0-9]{2,10}$", message = "Subject code must be 2-10 uppercase letters or numbers")
    private String code;
    
    private String description;
}