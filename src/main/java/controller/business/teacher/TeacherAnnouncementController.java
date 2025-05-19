package controller.business.teacher;

import dto.response.BaseResponse;
import dto.response.teacher.TeacherAnnouncementDetailResponse;
import dto.response.teacher.TeacherAnnouncementListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.teacher.TeacherAnnouncementService;

import java.util.List;

@RestController
@RequestMapping("/teacher/announcement")
@RequiredArgsConstructor
@PreAuthorize("hasRole('TEACHER')")
public class TeacherAnnouncementController {

    private final TeacherAnnouncementService teacherAnnouncementService;

    /**
     * Get announcements for the current week
     * Returns announcements targeted at teachers for the current week
     */
    @GetMapping("/week")
    public BaseResponse<List<TeacherAnnouncementListResponse>> getAnnouncementCurrentWeek() {
        return teacherAnnouncementService.getAnnouncementsForCurrentWeek();
    }

    /**
     * Get all announcements with pagination
     * Returns all announcements targeted at teachers
     */
    @GetMapping("/all")
    public BaseResponse<List<TeacherAnnouncementListResponse>> getAllAnnouncements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return teacherAnnouncementService.getAllAnnouncements(page, size);
    }
    
    /**
     * Get announcement details by ID
     * Returns detailed information for a specific announcement
     */
    @GetMapping("/{id}")
    public BaseResponse<TeacherAnnouncementDetailResponse> getAnnouncementById(@PathVariable Long id) {
        return teacherAnnouncementService.getAnnouncementById(id);
    }
}
