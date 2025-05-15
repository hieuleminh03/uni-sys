package controller.business.student;

import dto.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student/subject")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
public class StudentSubjectController {

    @GetMapping("/all")
    public BaseResponse<String> getAllSubjects() {
        return BaseResponse.ok(null, "All subjects retrieved successfully");
    }

    @GetMapping("/{id}")
    public BaseResponse<String> getSubjectById(@PathVariable int id) {
        // listing class with this subject & other information
        // not as detailed as admin view
        return BaseResponse.ok(null, "Subject information retrieved successfully");
    }

}