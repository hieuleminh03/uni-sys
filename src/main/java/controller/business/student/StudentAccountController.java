package controller.business.student;

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
import service.student.StudentAccountServiceImpl;

@RestController
@RequestMapping("/student/account")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
@Tag(name = "Student Account", description = "Student account management API endpoints")
public class StudentAccountController extends BaseController {

    private final StudentAccountServiceImpl studentAccountService;

    /**
     * For personal account management
     */
    @GetMapping()
    @Operation(summary = "Get student account information", description = "Retrieves the current student user's account information")
    public ResponseEntity<BaseResponse<AccountInformationResponse>> getAccountInformation() {
        return new ResponseEntity<>(studentAccountService.getAccountInformation(), HttpStatus.OK);
    }

    @PostMapping("/change-password")
    @Operation(summary = "Change student password", description = "Changes the current student user's password")
    public ResponseEntity<BaseResponse<String>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        return new ResponseEntity<>(studentAccountService.changePassword(request), HttpStatus.ACCEPTED);
    }
}
