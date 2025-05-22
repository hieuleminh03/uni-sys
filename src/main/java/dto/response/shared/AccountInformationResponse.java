package dto.response.shared;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import model.enums.AccountStatus;

@Getter
@Setter
@Builder
public class AccountInformationResponse {

    private String username;
    private AccountStatus status;
    private String fullName;
    private String email;
    private String avatarUrl;

}
