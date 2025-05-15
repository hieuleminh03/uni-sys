package controller.business.student;

import dto.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student/exam")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
public class StudentExamController {

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

}
