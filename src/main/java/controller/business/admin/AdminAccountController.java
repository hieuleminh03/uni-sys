package controller.business.admin;

import controller.BaseController;
import dto.request.admin.ChangePasswordRequest;
import dto.response.BaseResponse;
import dto.response.shared.AccountInformationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.admin.AdminAccountServiceImpl;

@RestController
@RequestMapping("/admin/account")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Account", description = "Admin account management API endpoints")
public class AdminAccountController extends BaseController {

    private final AdminAccountServiceImpl adminAccountService;

    /**
     * For personal account management
     */
    @GetMapping()
    @Operation(summary = "Get admin account information", description = "Retrieves the current admin user's account information")
    public ResponseEntity<BaseResponse<AccountInformationResponse>> getAccountInformation() {
        return new ResponseEntity<>(adminAccountService.getAccountInformation(), HttpStatus.OK);
    }

    @PostMapping("/change-password")
    @Operation(summary = "Change admin password", description = "Changes the current admin user's password")
    public ResponseEntity<BaseResponse<String>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        return new ResponseEntity<>(adminAccountService.changePassword(request), HttpStatus.ACCEPTED);
    }

    /**
     * For other account management
     */

    @GetMapping("/all")
    @Operation(summary = "Get all accounts", description = "Retrieves a list of all accounts in the system")
    public BaseResponse<String> getAllAccounts() {
        // should return enough for a screen
        return BaseResponse.ok(null, "List of all accounts retrieved successfully", null);
    }

    // for resetting other account's password
    @PostMapping("/reset-password")
    @Operation(summary = "Reset user password", description = "Resets a user's password and sends a notification")
    public BaseResponse<String> resetPassword() {
        // should also send announcement to the user
        return BaseResponse.accepted(null, "Password reset successfully");
    }
    // optional: change other account status to be "PENDING" -> for some investigations ?

    // for creating new account
    @PostMapping("/create")
    @Operation(summary = "Create a single account", description = "Creates a new user account")
    public BaseResponse<String> createSingleAccount() {
        return BaseResponse.created(null, "Account created successfully");
    }

    @PostMapping("/create/batch")
    @Operation(summary = "Create multiple accounts", description = "Creates multiple user accounts in batch")
    public BaseResponse<String> createBatchAccount() {
        return BaseResponse.created(null, "Batch account creation initiated");
    }

}
