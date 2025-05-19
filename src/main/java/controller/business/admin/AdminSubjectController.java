package controller.business.admin;

import dto.request.admin.CreateSubjectRequest;
import dto.request.admin.UpdateSubjectRequest;
import dto.response.BaseResponse;
import dto.response.admin.SubjectDetailResponse;
import dto.response.admin.SubjectListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.admin.AdminSubjectServiceImpl;

import java.util.Map;

@RestController
@RequestMapping("/admin/subject")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Subject Management", description = "Subject management API endpoints for administrators")
public class AdminSubjectController {

    private final AdminSubjectServiceImpl adminSubjectService;

    @GetMapping("/all")
    @Operation(summary = "Get all subjects", description = "Retrieves a list of all subjects with pagination")
    public BaseResponse<Map<String, Object>> getAllSubjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        
        Map<String, Object> subjects = adminSubjectService.getAllSubjects(pageable);
        return BaseResponse.ok(subjects, "All subjects retrieved successfully");
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get subject by ID", description = "Retrieves detailed information about a specific subject")
    public BaseResponse<SubjectDetailResponse> getSubjectById(@PathVariable Long id) {
        SubjectDetailResponse subject = adminSubjectService.getSubjectById(id);
        return BaseResponse.ok(subject, "Subject information retrieved successfully");
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new subject", description = "Creates a new subject with the provided details")
    public BaseResponse<SubjectListResponse> createSubject(@Valid @RequestBody CreateSubjectRequest request) {
        SubjectListResponse subject = adminSubjectService.createSubject(request);
        return BaseResponse.created(subject, "Subject created successfully");
    }

    @PatchMapping("/update")
    @Operation(summary = "Update an existing subject", description = "Updates the details of an existing subject")
    public BaseResponse<SubjectListResponse> updateSubject(@Valid @RequestBody UpdateSubjectRequest request) {
        SubjectListResponse subject = adminSubjectService.updateSubject(request);
        return BaseResponse.accepted(subject, "Subject updated successfully");
    }
}
