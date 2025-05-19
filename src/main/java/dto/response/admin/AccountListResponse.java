package dto.response.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.AccountStatus;
import model.enums.Role;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountListResponse {
    
    private Long id;
    private String username;
    private String fullName;
    private Role role;
    private String email;
    private String phoneNumber;
    private AccountStatus status;
    private LocalDateTime createdAt;
    
    // Additional fields that might be useful for filtering or display
    private Long userId;
    private Long studentId;
    private Long teacherId;
    private Long adminId;
}
