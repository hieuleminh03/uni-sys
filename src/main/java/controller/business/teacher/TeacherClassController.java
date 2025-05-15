package controller.business.teacher;

import dto.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teacher/class")
@RequiredArgsConstructor
@PreAuthorize("hasRole('TEACHER')")
public class TeacherClassController {

    // screen: class management
    // see classes that teacher is/was teaching only, others should be on the search screen

    @GetMapping("/all")
    public BaseResponse<String> getAllClasses() {
        // paging, can filter active classes
        // url: /student/class/all?page=0&size=10&active=true
        return BaseResponse.ok(null, "All classes retrieved successfully");
    }

    @GetMapping("/{id}")
    public BaseResponse<String> getStudentClass(@PathVariable int id) {
        // should return all possible data, contains tuition, attendance, exam,...
        // for classes that teacher is/was teaching
        return BaseResponse.ok(null, "Student class retrieved successfully");
    }
}
