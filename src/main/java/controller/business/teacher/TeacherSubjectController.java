package controller.business.teacher;

import dto.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teacher/subject")
@RequiredArgsConstructor
@PreAuthorize("hasRole('TEACHER')")
public class TeacherSubjectController {

    @GetMapping("/all")
    public BaseResponse<String> getAllSubjects() {
        return BaseResponse.ok(null, "All subjects retrieved successfully");
    }

    @GetMapping("/current")
    public BaseResponse<String> getCurrentSubjects() {
        // should return current subjects that teacher is teaching
        // basic information, should link to detailed view of class
        return BaseResponse.ok(null, "All subjects retrieved successfully");
    }

    @GetMapping("/{id}")
    public BaseResponse<String> getSubjectById(@PathVariable int id) {
        // listing class with this subject & other information
        // same as student view
        return BaseResponse.ok(null, "Subject information retrieved successfully");
    }

}
