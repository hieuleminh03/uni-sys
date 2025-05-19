package dto.request.admin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.AccountStatus;
import model.enums.Gender;
import model.enums.Role;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountRequest {
    
    @NotBlank(message = "Username cannot be blank")
    private String username;
    
    @NotNull(message = "Account status is required")
    private AccountStatus status;
    
    @NotNull(message = "Role is required")
    private Role role;
    
    @NotBlank(message = "Full name cannot be blank")
    private String fullName;
    
    private Date dob;
    
    private Gender gender;
    
    @Size(min = 8, max = 15, message = "Phone number must be between 8 and 15 characters")
    private String phoneNumber;
    
    @Email(message = "Email must be valid")
    private String email;
    
    private String avatarUrl;
}
