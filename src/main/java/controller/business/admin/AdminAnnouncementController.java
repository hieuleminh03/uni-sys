package controller.business.admin;

import controller.BaseController;
import dto.request.admin.CreateAnnouncementRequest;
import dto.request.admin.UpdateAnnouncementRequest;
import dto.response.BaseResponse;
import dto.response.admin.AnnouncementDetailResponse;
import dto.response.admin.AnnouncementListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.admin.AdminAnnouncementService;

import java.util.List;

@RestController
@RequestMapping("/admin/announcement")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Announcement Management", description = "Announcement management API endpoints for administrators")
public class AdminAnnouncementController extends BaseController {

    private final AdminAnnouncementService adminAnnouncementService;

    @GetMapping("/{id}")
    @Operation(summary = "Get announcement details", description = "Retrieves detailed information about a specific announcement")
    public ResponseEntity<BaseResponse<AnnouncementDetailResponse>> getAnnouncementById(@PathVariable Long id) {
        return ResponseEntity.ok(adminAnnouncementService.getAnnouncementById(id));
    }

    @GetMapping("/all")
    @Operation(summary = "Get all announcements", description = "Retrieves a list of all announcements with pagination")
    public ResponseEntity<BaseResponse<List<AnnouncementListResponse>>> getAllAnnouncements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(adminAnnouncementService.getAllAnnouncements(page, size));
    }

    @PostMapping("/create")
    @Operation(summary = "Create announcement", description = "Creates a new announcement for a specific target audience")
    public ResponseEntity<BaseResponse<String>> createAnnouncement(@Valid @RequestBody CreateAnnouncementRequest request) {
        return ResponseEntity.ok(adminAnnouncementService.createAnnouncement(request));
    }

    @PutMapping("/update")
    @Operation(summary = "Update announcement", description = "Updates an existing announcement")
    public ResponseEntity<BaseResponse<String>> updateAnnouncement(@Valid @RequestBody UpdateAnnouncementRequest request) {
        return ResponseEntity.ok(adminAnnouncementService.updateAnnouncement(request));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete announcement", description = "Deletes an announcement by ID")
    public ResponseEntity<BaseResponse<String>> deleteAnnouncement(@PathVariable Long id) {
        return ResponseEntity.ok(adminAnnouncementService.deleteAnnouncement(id));
    }
}
