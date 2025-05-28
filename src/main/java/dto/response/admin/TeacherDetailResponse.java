package dto.response.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.DiplomaLevel;
import model.enums.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDetailResponse {
    // Base User Information
    private Long id;
    private Long userId;
    private String username;
    private String fullName;
    private Gender gender;
    private LocalDate dob;
    private String email;
    private String phoneNumber;
    private String avatarUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Teacher-specific information
    private DiplomaLevel diplomaLevel;
    private String careerDesc;
}
