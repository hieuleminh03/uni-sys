package controller.business.student;

import dto.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student/homeroom")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
public class StudentHomeroomController {

    // a student should be in many homerooms throughout the years, and one at a time
    @GetMapping("/all")
    public BaseResponse<String> getAllHomerooms() {
        // enough information for outer listing screen
        return BaseResponse.ok(null, "List of all homerooms retrieved successfully", null);
    }

    // single homeroom information
    @GetMapping("/{id}")
    public BaseResponse<String> getHomeroomInformation() {
        // should limit to one homeroom
        return BaseResponse.ok(null, "Homeroom information retrieved successfully");
    }

}
