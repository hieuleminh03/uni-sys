package dto.request.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchCreateAccountRequest {
    
    @NotEmpty(message = "Account list cannot be empty")
    @Valid
    private List<CreateAccountRequest> accounts;
}
