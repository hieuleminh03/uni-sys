package controller.business.admin;

import dto.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/announcement")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminAnnouncementController {

    /**
     * Personal
     */
    @GetMapping("/")
    public BaseResponse<String> getAnnouncementInformation() {
        return BaseResponse.ok(null, "Announcement information retrieved successfully");
    }

    /**
     * Admin
     */
    @GetMapping("/all")
    public BaseResponse<String> getAllAnnouncements() {
        // should return enough for a screen
        return BaseResponse.accepted(null, "All announcements successfully");
    }

    @PostMapping("/create")
    public BaseResponse<String> createAnnouncement() {
        // note: should be once per role, no other fixes
        // consider removing tags
        return BaseResponse.created(null, "Announcement created successfully");
    }

    @PatchMapping("/update")
    public BaseResponse<String> updateAnnouncement() {
        return BaseResponse.accepted(null, "Announcement updated successfully");
    }

}
