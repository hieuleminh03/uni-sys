package controller.business.student;

import dto.response.BaseResponse;
import dto.response.admin.ScheduleExamListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import service.student.StudentExamServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/student/exam")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
public class StudentExamController {
    private final StudentExamServiceImpl studentExamServiceImpl;

    @GetMapping("/all")
    public BaseResponse<String> getAllExams() {
        // return a list of general information
        // could have filter for active exams
        // url: /student/exam/all?active=true|false
        return BaseResponse.ok(null, "List of all exams retrieved successfully", null);
    }

    @GetMapping("/byClass/{classId}")
    public BaseResponse<String> getExamsByClass(int classId) {
        // return a list of general information (in a class)
        return BaseResponse.ok(null, "List of exams for class " + classId + " retrieved successfully", null);
    }

    // consider adding table: re-grade requests by student
    @GetMapping("/detail/{examId}")
    public BaseResponse<String> getExam(@PathVariable String examId) {
        // return all detail of an exam for student: exam information, exam status, grade record,...
        // include grade information if available
        return BaseResponse.ok(null, "Exam retrieved successfully");
    }
    @GetMapping("/schedule-exam")
    public ResponseEntity<BaseResponse<List<ScheduleExamListResponse>>> scheduleExam(@RequestParam(defaultValue = "0") int page,
                                                                                     @RequestParam (defaultValue = "10") int size){

        return ResponseEntity.ok(studentExamServiceImpl.scheduleExam(page , size));
    }
}
