package service.admin;

import dto.request.admin.ChangePasswordRequest;
import dto.response.BaseResponse;
import dto.response.shared.AccountInformationResponse;
import exception.AuthenticationException;
import exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.Account;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.AccountRepository;
import repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminAccountServiceImpl {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
    
    @Transactional
    public BaseResponse<String> changePassword(ChangePasswordRequest request) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Account account = accountRepository.findByUsername(auth.getName())
                    .orElseThrow(() -> new RuntimeException("Account not found"));
            
            // Verify current password
            if (!passwordEncoder.matches(request.getCurrentPassword(), account.getPassword())) {
                throw new AuthenticationException("Current password is incorrect");
            }
            
            // Validate new password
            if (request.getNewPassword() == null || request.getNewPassword().isBlank()) {
                throw new BadRequestException("New password cannot be blank");
            }
            
            if (request.getNewPassword().length() < 8) {
                throw new BadRequestException("Password must be at least 8 characters long");
            }
            
            // Update password
            account.setPassword(passwordEncoder.encode(request.getNewPassword()));
            accountRepository.save(account);
            
            log.info("Password changed for user: {}", auth.getName());
            return BaseResponse.accepted(null, "Password changed successfully");
        } catch (BadRequestException e) {
            log.error("Bad request during password change", e);
            return BaseResponse.badRequest(e.getMessage(), e, null);
        } catch (AuthenticationException e) {
            log.error("Authentication error during password change", e);
            return BaseResponse.unauthorized(e.getMessage());
        } catch (Exception e) {
            log.error("Failed to change password", e);
            return BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to change password", e, null);
        }
    }
}
