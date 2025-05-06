package controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for student-specific API endpoints
 */
@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
public class StudentController {

    @GetMapping("/classes")
    public ResponseEntity<String> getStudentClasses() {
        // This is a placeholder method to demonstrate role-based security
        return ResponseEntity.ok("Student Classes Data");
    }
    
    @GetMapping("/examinations")
    public ResponseEntity<String> getStudentExaminations() {
        // This is a placeholder method to demonstrate role-based security
        return ResponseEntity.ok("Student Examination Data");
    }
    
    @GetMapping("/tuition")
    public ResponseEntity<String> getStudentTuition() {
        // This is a placeholder method to demonstrate role-based security
        return ResponseEntity.ok("Student Tuition Data");
    }
}
