package controller.business.teacher;

import dto.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teacher/account")
@RequiredArgsConstructor
@PreAuthorize("hasRole('TEACHER')")
public class TeacherAccountController {

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
    @GetMapping("/detail")
    public BaseResponse<String> getAccountDetail() {
        // this should return one account's detail (could be admin/student/teacher)
        return BaseResponse.ok(null, "Account detail retrieved successfully");
    }

}
