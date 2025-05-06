package controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for teacher-specific API endpoints
 */
@RestController
@RequestMapping("/api/v1/teacher")
@RequiredArgsConstructor
@PreAuthorize("hasRole('TEACHER')")
public class TeacherController {

    @GetMapping("/classes")
    public ResponseEntity<String> getTeacherClasses() {
        // This is a placeholder method to demonstrate role-based security
        return ResponseEntity.ok("Teacher Classes Data");
    }
    
    @GetMapping("/homerooms")
    public ResponseEntity<String> getTeacherHomerooms() {
        // This is a placeholder method to demonstrate role-based security
        return ResponseEntity.ok("Teacher Homeroom Data");
    }
}
