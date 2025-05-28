package dto.response.search;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import model.enums.AccountStatus;
import model.enums.Gender;
import model.enums.Role;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Builder
public class UserDetailResponse {
    private Long id;
    private String username;
    private AccountStatus accountStatus;
    private Role role;
    private String fullName;
    private LocalDate dob;
    private Gender gender;
    private String email;
    private String avatarUrl;
}