package controller.business.admin;

import controller.BaseController;
import dto.request.admin.*;
import dto.response.BaseResponse;
import dto.response.admin.HomeroomDetailResponse;
import dto.response.admin.HomeroomListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import model.enums.HomeroomStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import service.admin.AdminHomeroomServiceImpl;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/homeroom")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@Tag(name = "Admin Homeroom Management", description = "Homeroom management API endpoints for administrators")
public class AdminHomeroomController extends BaseController {

    private final AdminHomeroomServiceImpl adminHomeroomService;

    /**
     * Get all homerooms with pagination
     */
    @GetMapping("/all")
    @Operation(summary = "Get all homerooms", description = "Retrieves a list of all homerooms with pagination")
    public ResponseEntity<BaseResponse<List<HomeroomListResponse>>> getAllHomerooms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        return ResponseEntity.ok(adminHomeroomService.getAllHomerooms(page, size));
    }

    /**
     * Get homeroom by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get homeroom details", description = "Retrieves detailed information about a specific homeroom including student list")
    public ResponseEntity<BaseResponse<HomeroomDetailResponse>> getHomeroomById(@PathVariable Long id) {
        return ResponseEntity.ok(adminHomeroomService.getHomeroomById(id));
    }

    /**
     * Create a new homeroom
     */
    @PostMapping("/create")
    @Operation(summary = "Create homeroom", description = "Creates a new homeroom with an assigned teacher")
    public ResponseEntity<BaseResponse<String>> createHomeroom(@Valid @RequestBody CreateHomeroomRequest request) {
        return ResponseEntity.ok(adminHomeroomService.createHomeroom(request));
    }

    /**
     * Update an existing homeroom's name
     */
    @PutMapping("/update")
    @Operation(summary = "Update homeroom", description = "Updates an existing homeroom's name")
    public ResponseEntity<BaseResponse<String>> updateHomeroom(@Valid @RequestBody UpdateHomeroomRequest request) {
        return ResponseEntity.ok(adminHomeroomService.updateHomeroom(request));
    }

    /**
     * Update homeroom teacher
     */
    @PutMapping("/teacher")
    @Operation(summary = "Update homeroom teacher", description = "Assigns a different teacher to an existing homeroom")
    public ResponseEntity<BaseResponse<String>> updateHomeroomTeacher(
            @Valid @RequestBody UpdateHomeroomTeacherRequest request) {
        
        return ResponseEntity.ok(adminHomeroomService.updateHomeroomTeacher(request));
    }

    /**
     * Add a student to homeroom
     */
    @PostMapping("/students")
    @Operation(summary = "Add student to homeroom", description = "Adds a single student to a homeroom with specified status")
    public ResponseEntity<BaseResponse<String>> addStudentToHomeroom(
            @Valid @RequestBody AddStudentToHomeroomRequest request) {
        
        return ResponseEntity.ok(adminHomeroomService.addStudentToHomeroom(request));
    }

    /**
     * Batch add students to homeroom
     */
    @PostMapping("/students/batch")
    @Operation(summary = "Batch add students", description = "Adds multiple students to a homeroom with the same status")
    public ResponseEntity<BaseResponse<Map<String, Object>>> addStudentsToHomeroom(
            @Valid @RequestBody BatchAddStudentsRequest request) {
        
        return ResponseEntity.ok(adminHomeroomService.addStudentsToHomeroom(request));
    }

    /**
     * Remove a student from homeroom
     */
    @DeleteMapping("/students")
    @Operation(summary = "Remove student from homeroom", description = "Removes a single student from a homeroom")
    public ResponseEntity<BaseResponse<String>> removeStudentFromHomeroom(
            @Valid @RequestBody RemoveStudentFromHomeroomRequest request) {
        
        return ResponseEntity.ok(adminHomeroomService.removeStudentFromHomeroom(request));
    }

    /**
     * Batch remove students from homeroom
     */
    @DeleteMapping("/students/batch")
    @Operation(summary = "Batch remove students", description = "Removes multiple students from a homeroom")
    public ResponseEntity<BaseResponse<Map<String, Object>>> removeStudentsFromHomeroom(
            @Valid @RequestBody BatchRemoveStudentsRequest request) {
        
        return ResponseEntity.ok(adminHomeroomService.removeStudentsFromHomeroom(request));
    }

    /**
     * Update homeroom student status
     */
    @PutMapping("/students/{homeroomId}/{studentId}/status")
    @Operation(summary = "Update student status", description = "Updates a student's status in a homeroom (e.g., anticipated, graduated, expelled)")
    public ResponseEntity<BaseResponse<String>> updateHomeroomStudentStatus(
            @PathVariable Long homeroomId,
            @PathVariable Long studentId,
            @RequestParam HomeroomStatus status) {
        
        return ResponseEntity.ok(adminHomeroomService.updateHomeroomStudentStatus(homeroomId, studentId, status));
    }
}
