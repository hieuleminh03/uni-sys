package controller.business.student;

import dto.response.BaseResponse;
import dto.response.student.StudentAnnouncementDetailResponse;
import dto.response.student.StudentAnnouncementListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import service.student.StudentAnnouncementService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/student/announcement")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
public class StudentAnnouncementController {

    private final StudentAnnouncementService studentAnnouncementService;

    /**
     * Get announcements for the current week
     * Returns announcements targeted at students for the current week
     */
    @GetMapping("/week")
    public BaseResponse<List<StudentAnnouncementListResponse>> getAnnouncementCurrentWeek() {
        return studentAnnouncementService.getAnnouncementsForCurrentWeek();
    }

    /**
     * Get all announcements with pagination
     * Returns all announcements targeted at students
     */
    @GetMapping("/all")
    public BaseResponse<List<StudentAnnouncementListResponse>> getAllAnnouncements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return studentAnnouncementService.getAllAnnouncements(page, size);
    }
    
    /**
     * Get announcement details by ID
     * Returns detailed information for a specific announcement
     */
    @GetMapping("/{id}")
    public BaseResponse<StudentAnnouncementDetailResponse> getAnnouncementById(@PathVariable Long id) {
        return studentAnnouncementService.getAnnouncementById(id);
    }
}
