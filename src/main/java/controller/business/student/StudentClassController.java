package controller.business.student;

import dto.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student/class")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
public class StudentClassController {

    // for class that is currently in only, others should be on search page

    @GetMapping("/all")
    public BaseResponse<String> getAllClasses() {
        // paging, allow filter active classes
        // url: /student/class/all?page=0&size=10&active=true
        return BaseResponse.ok(null, "All classes retrieved successfully");
    }

    @GetMapping("/{id}")
    public BaseResponse<String> getStudentClass(@PathVariable int id) {
        // should return all possible data, contains tuition, attendance, exam,...
        return BaseResponse.ok(null, "Student class retrieved successfully");
    }

}
