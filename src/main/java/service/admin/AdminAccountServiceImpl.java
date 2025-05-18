package service.admin;

import dto.response.BaseResponse;
import dto.response.shared.AccountInformationResponse;
import lombok.RequiredArgsConstructor;
import model.Account;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import repository.AccountRepository;

@Service
@RequiredArgsConstructor
public class AdminAccountServiceImpl {

    private final AccountRepository accountRepository;

    public BaseResponse<AccountInformationResponse> getAccountInformation() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Account account = accountRepository.findByUsername(auth.getName())
                    .orElseThrow(() -> new RuntimeException("Account not found"));
            AccountInformationResponse response = AccountInformationResponse.builder()
                    .username(account.getUsername())
                    .status(account.getStatus())
                    .build();
            return BaseResponse.ok(response,"Account information retrieved successfully");
        } catch (Exception e) {
            return BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to retrieve account information", e, null);
        }
    }
}
