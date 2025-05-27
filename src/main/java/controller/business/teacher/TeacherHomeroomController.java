package controller.business.teacher;

import controller.BaseController;
import dto.request.admin.HomeroomStatusResquest;
import dto.response.BaseResponse;
import dto.response.teacher.TeacherHomeroomDetailResponse;
import dto.response.teacher.TeacherHomeroomListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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
     * Get homeroom details by ID
     */
    @GetMapping("")
    @Operation(summary = "Get homeroom details", description = "Retrieves detailed information about a specific homeroom")
    public ResponseEntity<BaseResponse<TeacherHomeroomDetailResponse>> getHomeroom() {
        return ResponseEntity.ok(teacherHomeroomService.getHomeroom());
    }

    @PutMapping("{studentId}/status")
    @Operation(summary = "Update status studnet", description = "Update status student in homeroom")
    public ResponseEntity<BaseResponse<String>> updateStatusStudent(@PathVariable Long studentId, @RequestBody HomeroomStatusResquest homeroomStatusResquest) {
        return ResponseEntity.ok(teacherHomeroomService.updateStatusStudent(studentId, homeroomStatusResquest));
    }
}
