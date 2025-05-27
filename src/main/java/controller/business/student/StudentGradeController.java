package controller.business.student;

import dto.response.BaseResponse;
import dto.response.admin.TuitionListResponse;
import dto.response.student.StudentGradeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.student.StudentGradeServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/student/grade")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
public class StudentGradeController {
    private final StudentGradeServiceImpl studentGradeService;
    @GetMapping("/all")
    public ResponseEntity<BaseResponse<List<StudentGradeResponse>>> getAllGrade(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(studentGradeService.getGrades(page, size));
    }
}
