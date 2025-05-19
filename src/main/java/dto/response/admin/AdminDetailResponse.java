package dto.response.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.Gender;
import model.enums.Role;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDetailResponse {
    private Long id;
    private Long userId;
    private String username;
    private String fullName;
    private Gender gender;
    private Date dob;
    private String email;
    private String phoneNumber;
    private String avatarUrl;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
