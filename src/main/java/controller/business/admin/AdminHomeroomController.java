package controller.business.admin;

import dto.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/homeroom")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminHomeroomController {

    /**
     * Admin related, since no admin attached with any homeroom
     */

    @GetMapping("/all")
    public BaseResponse<String> getAllHomerooms() {
        // basic information
        return BaseResponse.ok(null, "All homerooms retrieved successfully");
    }

    @GetMapping("/{id}")
    public BaseResponse<String> getHomeroomById() {
        // with detail
        return BaseResponse.ok(null, "Homeroom retrieved successfully");
    }

    @PostMapping("/create")
    public BaseResponse<String> createHomeroom() {
        return BaseResponse.created(null, "Homeroom created successfully");
    }

    @PatchMapping("/students/infor/update")
    public BaseResponse<String> updateHomeroomStudent() {
        return BaseResponse.accepted(null, "Homeroom student information updated successfully");
    }

    /**
     * Member list update
     */
    @PostMapping("/students/add")
    public BaseResponse<String> addStudentToHomeroom() {
        return BaseResponse.created(null, "Student added to homeroom successfully");
    }

    @PostMapping("/students/add/batch")
    public BaseResponse<String> addStudentsToHomeroom() {
        return BaseResponse.created(null, "Students added to homeroom successfully");
    }

    @DeleteMapping("/students/remove")
    public BaseResponse<String> removeStudentFromHomeroom() {
        return BaseResponse.accepted(null, "Student removed from homeroom successfully");
    }

    @DeleteMapping("/students/remove/batch")
    public BaseResponse<String> removeStudentsFromHomeroom() {
        return BaseResponse.accepted(null, "Students removed from homeroom successfully");
    }

    @PostMapping("/teacher/update")
    public BaseResponse<String> updateHomeroomTeacher() {
        // should handle all case: assign, change, remove
        return BaseResponse.accepted(null, "Homeroom teacher updated successfully");
    }
}
