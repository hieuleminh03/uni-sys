package controller.business.teacher;

import controller.BaseController;
import dto.response.BaseResponse;
import dto.response.teacher.TeacherHomeroomDetailResponse;
import dto.response.teacher.TeacherHomeroomListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.teacher.TeacherHomeroomServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/teacher/homeroom")
@RequiredArgsConstructor
@PreAuthorize("hasRole('TEACHER')")
@Tag(name = "Teacher Homeroom", description = "Homeroom API endpoints for teachers")
public class TeacherHomeroomController extends BaseController {

    private final TeacherHomeroomServiceImpl teacherHomeroomService;

    /**
     * Get all homerooms for the current teacher
     */
    @GetMapping("/all")
    @Operation(summary = "Get all homerooms", description = "Retrieves a list of all homerooms for the current teacher")
    public ResponseEntity<BaseResponse<List<TeacherHomeroomListResponse>>> getAllHomerooms() {
        return ResponseEntity.ok(teacherHomeroomService.getAllHomerooms());
    }

    /**
     * Get homeroom details by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get homeroom details", description = "Retrieves detailed information about a specific homeroom")
    public ResponseEntity<BaseResponse<TeacherHomeroomDetailResponse>> getHomeroomById(@PathVariable Long id) {
        return ResponseEntity.ok(teacherHomeroomService.getHomeroomById(id));
    }
}
