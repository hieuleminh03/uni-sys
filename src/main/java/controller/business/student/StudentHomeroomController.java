package controller.business.student;

import controller.BaseController;
import dto.response.BaseResponse;
import dto.response.student.StudentHomeroomDetailResponse;
import dto.response.student.StudentHomeroomListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.student.StudentHomeroomServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/student/homeroom")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
@Tag(name = "Student Homeroom", description = "Homeroom API endpoints for students")
public class StudentHomeroomController extends BaseController {

    private final StudentHomeroomServiceImpl studentHomeroomService;

    /**
     * Get all homerooms for the current student
     */
    @GetMapping("/all")
    @Operation(summary = "Get all homerooms", description = "Retrieves a list of all homerooms for the current student")
    public ResponseEntity<BaseResponse<List<StudentHomeroomListResponse>>> getAllHomerooms() {
        return ResponseEntity.ok(studentHomeroomService.getAllHomerooms());
    }

    /**
     * Get homeroom details by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get homeroom details", description = "Retrieves detailed information about a specific homeroom")
    public ResponseEntity<BaseResponse<StudentHomeroomDetailResponse>> getHomeroomById(@PathVariable Long id) {
        return ResponseEntity.ok(studentHomeroomService.getHomeroomById(id));
    }
}
