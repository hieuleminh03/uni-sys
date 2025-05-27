package controller.business.student;

import dto.response.BaseResponse;
import dto.response.admin.TuitionListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.student.StudentTuitionServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/student/tuition")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
public class StudentTuitionController {

    private final StudentTuitionServiceImpl studentTuitionService;

    @GetMapping("/all")
    public ResponseEntity<BaseResponse<List<TuitionListResponse>>> getAllTuition(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
         return ResponseEntity.ok(studentTuitionService.getAllTuition(page, size));
    }

}
