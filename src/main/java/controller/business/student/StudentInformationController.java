package controller.business.student;

import controller.BaseController;
import dto.response.BaseResponse;
import dto.response.admin.StudentDetailResponse;
import dto.request.student.StudentUpdateInformationRequest;
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
import service.student.StudentInformationService;

@RestController
@RequestMapping("/student/information")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('STUDENT')")
@Tag(name = "Student Information", description = "Student information management API endpoints for students")
public class StudentInformationController extends BaseController {

    private final StudentInformationService studentInformationService;

    /**
     * Get student's own information
     */
    @GetMapping()
    @Operation(summary = "Get student's own information", description = "Retrieves detailed information about the logged-in student")
    public ResponseEntity<BaseResponse<StudentDetailResponse>> getStudentInformation() {
        return ResponseEntity.ok(studentInformationService.getStudentInformation());
    }

    /**
     * Update student's own information
     */
    @PutMapping()
    @Operation(summary = "Update student's own information", description = "Updates the information for the logged-in student")
    public ResponseEntity<BaseResponse<String>> updateStudentInformation(@Valid @RequestBody StudentUpdateInformationRequest request) {
        return ResponseEntity.ok(studentInformationService.updateStudentInformation(request));
    }
}
