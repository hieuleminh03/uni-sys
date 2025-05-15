package controller.business.teacher;

import dto.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teacher/announcement")
@RequiredArgsConstructor
@PreAuthorize("hasRole('TEACHER')")
public class TeacherAnnouncementController {

    /**
     * Personal
     */
    @GetMapping("/week")
    public BaseResponse<String> getAnnouncementCurrentWeek() {
        return BaseResponse.ok(null, "Announcement information in current week retrieved successfully");
    }

    @GetMapping("/")
    public BaseResponse<String> getAnnouncementInformation() {
        // get all announcements
        // should have time
        return BaseResponse.ok(null, "Announcement information retrieved successfully");
    }

}
