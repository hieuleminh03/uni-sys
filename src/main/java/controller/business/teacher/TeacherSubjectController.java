package controller.business.teacher;

import controller.BaseController;
import dto.response.BaseResponse;
import dto.response.teacher.TeacherSubjectDetailResponse;
import dto.response.teacher.TeacherSubjectListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.teacher.TeacherSubjectServiceImpl;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/teacher/subject")
@RequiredArgsConstructor
@PreAuthorize("hasRole('TEACHER')")
@Tag(name = "Teacher Subject Management", description = "Subject API endpoints for teachers")
public class TeacherSubjectController extends BaseController {

    private final TeacherSubjectServiceImpl teacherSubjectService;

    /**
     * Get all subjects with pagination
     */
    @GetMapping("/all")
    @Operation(summary = "Get all subjects", description = "Retrieves a list of all subjects with pagination")
    public ResponseEntity<BaseResponse<Map<String, Object>>> getAllSubjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(teacherSubjectService.getAllSubjects(pageable));
    }

    /**
     * Get current subjects that the teacher is teaching
     */
    @GetMapping("/current")
    @Operation(summary = "Get current subjects", description = "Retrieves a list of subjects that the teacher is currently teaching")
    public ResponseEntity<BaseResponse<List<TeacherSubjectListResponse>>> getCurrentSubjects() {
        return ResponseEntity.ok(teacherSubjectService.getCurrentSubjects());
    }

    /**
     * Get subject by ID with class information
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get subject by ID", description = "Retrieves detailed information about a specific subject")
    public ResponseEntity<BaseResponse<TeacherSubjectDetailResponse>> getSubjectById(@PathVariable Long id) {
        return ResponseEntity.ok(teacherSubjectService.getSubjectById(id));
    }
}
