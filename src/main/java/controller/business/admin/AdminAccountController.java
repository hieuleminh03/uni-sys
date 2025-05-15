package controller.business.admin;

import dto.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/account")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminAccountController {

    /**
     * For personal account management
     */
    @GetMapping("/")
    public BaseResponse<String> getAccountInformation() {
        return BaseResponse.ok(null, "Account information retrieved successfully");
    }

    @PatchMapping("/")
    public BaseResponse<String> updateAccountInformation() {
        return BaseResponse.accepted(null, "Account information updated successfully");
    }

    @PostMapping("/change-password")
    public BaseResponse<String> changePassword() {
        return BaseResponse.accepted(null, "Password changed successfully");
    }

    /**
     * For other account management
     */

    @GetMapping("/all")
    public BaseResponse<String> getAllAccounts() {
        // should return enough for a screen
        return BaseResponse.ok(null, "List of all accounts retrieved successfully", null);
    }

    // for resetting other account's password
    @PostMapping("/reset-password")
    public BaseResponse<String> resetPassword() {
        // should also send announcement to the user
        return BaseResponse.accepted(null, "Password reset successfully");
    }
    // optional: change other account status to be "PENDING" -> for some investigations ?

    // for creating new account
    @PostMapping("/create")
    public BaseResponse<String> createSingleAccount() {
        return BaseResponse.created(null, "Account created successfully");
    }

    @PostMapping("/create/batch")
    public BaseResponse<String> createBatchAccount() {
        return BaseResponse.created(null, "Batch account creation initiated");
    }


}