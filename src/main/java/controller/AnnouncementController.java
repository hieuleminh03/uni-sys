package controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for announcement-related endpoints
 * Note: This controller has endpoints for all roles, with role-based authorization
 */
@RestController
@RequestMapping("/api/v1/announcements")
@RequiredArgsConstructor
public class AnnouncementController {

    @GetMapping
    public ResponseEntity<String> getAnnouncementsForCurrentUser() {
        // This method would get announcements appropriate for the current user's role
        return ResponseEntity.ok("Announcements for current user");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/create")
    public ResponseEntity<String> createAnnouncement() {
        // Only admins can create announcements
        return ResponseEntity.ok("Create announcement - Admin only");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/manage")
    public ResponseEntity<String> manageAnnouncements() {
        // Only admins can manage all announcements
        return ResponseEntity.ok("Manage announcements - Admin only");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @GetMapping("/teacher")
    public ResponseEntity<String> getTeacherAnnouncements() {
        // Both admins and teachers can access this
        return ResponseEntity.ok("Teacher announcements");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    @GetMapping("/student")
    public ResponseEntity<String> getStudentAnnouncements() {
        // Both admins and students can access this
        return ResponseEntity.ok("Student announcements");
    }
}
