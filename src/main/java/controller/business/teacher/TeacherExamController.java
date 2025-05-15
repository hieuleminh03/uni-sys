package controller.business.teacher;

import dto.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teacher/exam")
@RequiredArgsConstructor
@PreAuthorize("hasRole('TEACHER')")
public class TeacherExamController {

    @GetMapping("/all")
    public BaseResponse<String> getAllExams() {
        // return a list of general information (for class that teacher is/was teaching)
        // could have filter for active exams
        // url: /teacher/exam/all?active=true|false
        return BaseResponse.ok(null, "List of all exams retrieved successfully", null);
    }

    @GetMapping("/byClass/{classId}")
    public BaseResponse<String> getExamsByClass(@PathVariable String classId) {
        // return a list of general information (in a class that teacher is/was teaching)
        return BaseResponse.ok(null, "List of exams for class " + classId + " retrieved successfully", null);
    }

    @GetMapping("/detail/{examId}")
    public BaseResponse<String> getExam(@PathVariable String examId) {
        // return all detail of an exam for student: exam information, exam status, grade record,...
        // include grade information if available
        return BaseResponse.ok(null, "Exam retrieved successfully");
    }

    @PostMapping("/create")
    public BaseResponse<String> createExam() {
        // teacher could create a regular exam, not final/midterm exam
        return BaseResponse.created(null, "Exam created successfully");
    }

    @PostMapping("/grade")
    public BaseResponse<String> gradeExam() {
        // teacher could grade an exam
        return BaseResponse.accepted(null, "Exam graded successfully");
    }
}
