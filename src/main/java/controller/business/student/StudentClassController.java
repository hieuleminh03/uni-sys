package controller.business.student;

import dto.response.BaseResponse;
import dto.response.admin.ClassDetailResponse;
import dto.response.admin.ClassListResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import service.student.StudentServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/student/class")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
public class StudentClassController {

    // for class that is currently in only, others should be on search page
    private final StudentServiceImpl studentClassService;

    @GetMapping("/all")
    @Operation(summary = "Get All class", description = "")
    public BaseResponse<List<ClassListResponse>> getAllClasses(@RequestParam(defaultValue = "1") int page,
                                                               @RequestParam(defaultValue = "10") int size){


        return studentClassService.getAllClasses(page,size);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Class Details")
    public BaseResponse<ClassDetailResponse> getClassDetails(@PathVariable Long id) {

        return BaseResponse.ok(studentClassService.getClassDetailsById(id), "Class members retrieved successfully");
    }

}
