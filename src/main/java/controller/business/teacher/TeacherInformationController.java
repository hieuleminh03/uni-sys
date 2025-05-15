package controller.business.teacher;

import dto.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teacher/information")
@RequiredArgsConstructor
@PreAuthorize("hasRole('TEACHER')")
public class TeacherInformationController {

    @GetMapping("/")
    public BaseResponse<String> getTeacherInformation() {
        return BaseResponse.ok(null, "Teacher information retrieved successfully");
    }

    @PatchMapping("/")
    public BaseResponse<String> updateTeacherInformation() {
        return BaseResponse.accepted(null, "Teacher information updated successfully");
    }

}

