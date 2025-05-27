package controller.business.teacher;

import dto.response.BaseResponse;
import dto.response.admin.ClassDetailResponse;
import dto.response.admin.ClassListResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import service.teacher.TeacherClassServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/teacher/class")
@RequiredArgsConstructor
@PreAuthorize("hasRole('TEACHER')")
public class TeacherClassController {

    // screen: class management
    // see classes that teacher is/was teaching only, others should be on the search screen

    private final TeacherClassServiceImpl teacherClassServiceImpl;

    @GetMapping("/all")
    @Operation(summary = "Get All class", description = "")
    public BaseResponse<List<ClassListResponse>> getAllClasses(@RequestParam(defaultValue = "1") int page,
                                                               @RequestParam(defaultValue = "10") int size){


        return teacherClassServiceImpl.getAllClasses(page,size);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Class Details")
    public BaseResponse<ClassDetailResponse> getClassDetails(@PathVariable Long id) {

        return BaseResponse.ok(teacherClassServiceImpl.getClassDetailsById(id), "Class members retrieved successfully");
    }
}
