package dto.request.teacher;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.DiplomaLevel;
import model.enums.Gender;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherUpdateAccountRequest {
    
    @NotBlank(message = "Full name cannot be blank")
    private String fullName;
    
    private LocalDate dob;
    
    private Gender gender;
    
    @Size(min = 8, max = 15, message = "Phone number must be between 8 and 15 characters")
    private String phoneNumber;
    
    @Email(message = "Email must be valid")
    private String email;
    
    private String avatarUrl;
    
    // Teacher-specific fields
    private String careerDesc;
    
    private DiplomaLevel diplomaLevel;
}
