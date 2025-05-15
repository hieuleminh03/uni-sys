package controller.business.teacher;

import dto.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teacher/tuition")
@RequiredArgsConstructor
@PreAuthorize("hasRole('TEACHER')")
public class TeacherTuitionController {

    @GetMapping("/all")
    public BaseResponse<String> getAllTuition() {
        // this should be for viewing only
        // allows filtering by class, tuition type, tuition status (class should be the one that teacher is/was teaching)
        return BaseResponse.ok(null, "List of all tuition retrieved successfully", null);
    }
}
