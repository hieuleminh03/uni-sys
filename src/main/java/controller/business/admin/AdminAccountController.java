package controller.business.admin;

import controller.BaseController;
import dto.request.admin.BatchCreateAccountRequest;
import dto.request.admin.ChangePasswordRequest;
import dto.request.admin.CreateAccountRequest;
import dto.request.admin.ResetPasswordRequest;
import dto.response.BaseResponse;
import dto.response.admin.AccountListResponse;
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

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<BaseResponse<List<AccountListResponse>>> getAllAccounts() {
        return new ResponseEntity<>(adminAccountService.getAllAccounts(), HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset user password", description = "Resets a user's password and sends a notification")
    public ResponseEntity<BaseResponse<String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        return new ResponseEntity<>(adminAccountService.resetPassword(request), HttpStatus.ACCEPTED);
    }

    @PostMapping("/create")
    @Operation(summary = "Create a single account", description = "Creates a new user account")
    public ResponseEntity<BaseResponse<String>> createSingleAccount(@Valid @RequestBody CreateAccountRequest request) {
        return new ResponseEntity<>(adminAccountService.createSingleAccount(request), HttpStatus.CREATED);
    }


}
