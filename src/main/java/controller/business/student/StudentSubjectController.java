package controller.business.student;

import controller.BaseController;
import dto.response.BaseResponse;
import dto.response.student.StudentSubjectDetailResponse;
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
import service.student.StudentSubjectServiceImpl;

import java.util.Map;

@RestController
@RequestMapping("/student/subject")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
@Tag(name = "Student Subject Management", description = "Subject API endpoints for students")
public class StudentSubjectController extends BaseController {

    private final StudentSubjectServiceImpl studentSubjectService;

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
        return ResponseEntity.ok(studentSubjectService.getAllSubjects(pageable));
    }

    /**
     * Get subject by ID with class information
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get subject by ID", description = "Retrieves detailed information about a specific subject")
    public ResponseEntity<BaseResponse<StudentSubjectDetailResponse>> getSubjectById(@PathVariable Long id) {
        return ResponseEntity.ok(studentSubjectService.getSubjectById(id));
    }
}