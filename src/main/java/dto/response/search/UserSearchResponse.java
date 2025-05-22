package dto.response.search;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import model.enums.Role;

@Getter
@Setter
@Builder
public class UserSearchResponse {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private Role role;
    private String avatarUrl;
}