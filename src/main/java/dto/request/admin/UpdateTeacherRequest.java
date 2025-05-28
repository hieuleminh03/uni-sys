package dto.request.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.DiplomaLevel;
import model.enums.Gender;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTeacherRequest {
    @NotNull(message = "Teacher ID is required")
    private Long teacherId;
    
    // User information
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;
    private LocalDate dob;
    private Gender gender;
    
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be valid")
    private String phoneNumber;
    
    @Email(message = "Email must be valid")
    private String email;
    private String avatarUrl;
    
    // Teacher-specific information
    private DiplomaLevel diplomaLevel;
    private String careerDesc;
}
