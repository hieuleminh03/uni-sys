package controller.business.student;

import dto.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student/information")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
public class StudentInformationController {

    @GetMapping("/")
    public BaseResponse<String> getStudentInformation() {
        return BaseResponse.ok(null, "Student information retrieved successfully");
    }

    @PatchMapping("/")
    public BaseResponse<String> updateStudentInformation() {
        return BaseResponse.accepted(null, "Student information updated successfully");
    }

}
