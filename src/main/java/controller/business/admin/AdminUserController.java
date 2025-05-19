package controller.business.admin;

import controller.BaseController;
import dto.request.admin.AdminUpdateProfileRequest;
import dto.request.admin.UpdateStudentRequest;
import dto.request.admin.UpdateTeacherRequest;
import dto.response.BaseResponse;
import dto.response.admin.AdminDetailResponse;
import dto.response.admin.StudentDetailResponse;
import dto.response.admin.TeacherDetailResponse;
import dto.response.admin.UserListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.admin.AdminUserServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin User Management", description = "User management API endpoints for administrators")
public class AdminUserController extends BaseController {

    private final AdminUserServiceImpl adminUserService;

    /**
     * Get all users
     */
    @GetMapping("/all")
    @Operation(summary = "Get all users", description = "Retrieves a list of all users in the system with basic information")
    public ResponseEntity<BaseResponse<List<UserListResponse>>> getAllUsers() {
        return ResponseEntity.ok(adminUserService.getAllUsers());
    }

    /**
     * Get admin details
     */
    @GetMapping("/admin/{adminId}")
    @Operation(summary = "Get admin details", description = "Retrieves detailed information about a specific admin including personal details")
    public ResponseEntity<BaseResponse<AdminDetailResponse>> getAdminDetail(@PathVariable Long adminId) {
        return ResponseEntity.ok(adminUserService.getAdminDetail(adminId));
    }
    
    /**
     * Get student details
     */
    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get student details", description = "Retrieves detailed information about a specific student including personal and educational details")
    public ResponseEntity<BaseResponse<StudentDetailResponse>> getStudentDetail(@PathVariable Long studentId) {
        return ResponseEntity.ok(adminUserService.getStudentDetail(studentId));
    }
    
    /**
     * Get teacher details
     */
    @GetMapping("/teacher/{teacherId}")
    @Operation(summary = "Get teacher details", description = "Retrieves detailed information about a specific teacher including personal and professional details")
    public ResponseEntity<BaseResponse<TeacherDetailResponse>> getTeacherDetail(@PathVariable Long teacherId) {
        return ResponseEntity.ok(adminUserService.getTeacherDetail(teacherId));
    }

    /**
     * Update student information
     */
    @PutMapping("/update/student")
    @Operation(summary = "Update student information", description = "Updates student information including basic personal details and student-specific information")
    public ResponseEntity<BaseResponse<String>> updateStudentInformation(@Valid @RequestBody UpdateStudentRequest request) {
        return ResponseEntity.ok(adminUserService.updateStudentInformation(request));
    }
    
    /**
     * Update teacher information
     */
    @PutMapping("/update/teacher")
    @Operation(summary = "Update teacher information", description = "Updates teacher information including basic personal details and teacher-specific information")
    public ResponseEntity<BaseResponse<String>> updateTeacherInformation(@Valid @RequestBody UpdateTeacherRequest request) {
        return ResponseEntity.ok(adminUserService.updateTeacherInformation(request));
    }
    
    /**
     * Update admin profile
     */
    @PutMapping("/update/admin")
    @Operation(summary = "Update admin profile", description = "Updates admin personal information in the user table")
    public ResponseEntity<BaseResponse<String>> updateAdminProfile(@Valid @RequestBody AdminUpdateProfileRequest request) {
        return ResponseEntity.ok(adminUserService.updateAdminProfile(request));
    }
}
