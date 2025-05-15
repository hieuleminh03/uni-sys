package controller.business.admin;

import dto.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    /**
     * Personal
     */
    @GetMapping("/")
    public BaseResponse<String> getUserInformation() {
        return BaseResponse.ok(null, "User information retrieved successfully");
    }

    @PatchMapping("/")
    public BaseResponse<String> updateUserInformation() {
        return BaseResponse.accepted(null, "User information updated successfully");
    }

    /**
     * Other users
     */
    // other users, include role-based (optional) in params
    @GetMapping("/all")
    public BaseResponse<String> getAllUsers() {
        // should return enough for a screen (general information)
        return BaseResponse.ok(null, "List of all users retrieved successfully", null);
    }

    // get detail with role-based data (student information, teacher information table)
    @GetMapping("/student")
    public BaseResponse<String> getStudentInformation() {
        // url: /admin/users/student/{id}
        return BaseResponse.ok(null, "Student information retrieved successfully", null);
    }

    @GetMapping("/teacher")
    public BaseResponse<String> getTeacherInformation() {
        // url: /admin/users/teacher/{id}
        return BaseResponse.ok(null, "Teacher information retrieved successfully", null);
    }

    // for changing other account's information
    // should not affect other admin
    @PatchMapping("/update")
    public BaseResponse<String> updateOtherUserInformation() {
        // should also send announcement to the user
        return BaseResponse.accepted(null, "User information updated successfully");
    }

}
