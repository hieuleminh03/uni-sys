package controller.business.teacher;

import controller.BaseController;
import dto.response.BaseResponse;
import dto.response.admin.TeacherDetailResponse;
import dto.request.teacher.TeacherUpdateInformationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.teacher.TeacherInformationService;

@RestController
@RequestMapping("/teacher/information")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('TEACHER')")
@Tag(name = "Teacher Information", description = "Teacher information management API endpoints for teachers")
public class TeacherInformationController extends BaseController {

    private final TeacherInformationService teacherInformationService;

    /**
     * Get teacher's own information
     */
    @GetMapping()
    @Operation(summary = "Get teacher's own information", description = "Retrieves detailed information about the logged-in teacher")
    public ResponseEntity<BaseResponse<TeacherDetailResponse>> getTeacherInformation() {
        return ResponseEntity.ok(teacherInformationService.getTeacherInformation());
    }

    /**
     * Update teacher's own information
     */
    @PutMapping()
    @Operation(summary = "Update teacher's own information", description = "Updates the information for the logged-in teacher")
    public ResponseEntity<BaseResponse<String>> updateTeacherInformation(@Valid @RequestBody TeacherUpdateInformationRequest request) {
        return ResponseEntity.ok(teacherInformationService.updateTeacherInformation(request));
    }
}
