package controller.business.teacher;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.teacher.TeacherAccountServiceImpl;

@RestController
@RequestMapping("/teacher/account")
@RequiredArgsConstructor
@PreAuthorize("hasRole('TEACHER')")
@Tag(name = "Teacher Account", description = "Teacher account management API endpoints")
public class TeacherAccountController extends BaseController {

    private final TeacherAccountServiceImpl teacherAccountService;

    /**
     * For personal account management
     */
    @GetMapping()
    @Operation(summary = "Get teacher account information", description = "Retrieves the current teacher user's account information")
    public ResponseEntity<BaseResponse<AccountInformationResponse>> getAccountInformation() {
        return new ResponseEntity<>(teacherAccountService.getAccountInformation(), HttpStatus.OK);
    }

    @PostMapping("/change-password")
    @Operation(summary = "Change teacher password", description = "Changes the current teacher user's password")
    public ResponseEntity<BaseResponse<String>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        return new ResponseEntity<>(teacherAccountService.changePassword(request), HttpStatus.ACCEPTED);
    }

    /**
     * For other account management
     */
    @GetMapping("/detail/{id}")
    @Operation(summary = "Get teacher account detail by ID (to see other teacher)", description = "Retrieves the account detail for a specific teacher by ID")
    public ResponseEntity<BaseResponse<AccountInformationResponse>> getTeacherDetailById(@PathVariable Long id) {
        return new ResponseEntity<>(teacherAccountService.getTeacherDetailById(id), HttpStatus.OK);
    }
}
