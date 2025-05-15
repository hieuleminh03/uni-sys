package controller.business.teacher;

import dto.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teacher/homeroom")
@RequiredArgsConstructor
@PreAuthorize("hasRole('TEACHER')")
public class TeacherHomeroomController {

    // history, current homerooms list
    @GetMapping("/all")
    public BaseResponse<String> getAllHomerooms() {
        // enough information for outer listing screen
        return BaseResponse.ok(null, "List of all homerooms retrieved successfully", null);
    }

    // single homeroom information
    @GetMapping("/{id}")
    public BaseResponse<String> getHomeroomInformation() {
        return BaseResponse.ok(null, "Homeroom information retrieved successfully");
    }
}
