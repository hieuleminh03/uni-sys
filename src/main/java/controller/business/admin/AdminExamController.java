package controller.business.admin;

import dto.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/exam")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminExamController {

    @GetMapping("/all")
    public BaseResponse<String> getAllExams() {
        // return a list of general information
        return BaseResponse.ok(null, "List of all exams retrieved successfully", null);
    }

    @GetMapping("/byClass/{classId}")
    public BaseResponse<String> getExamsByClass(@PathVariable int classId) {
        // return a list of general information (in a class)
        return BaseResponse.ok(null, "List of exams for class " + classId + " retrieved successfully", null);
    }

    @GetMapping("/detail/{examId}")
    public BaseResponse<String> getExam(@PathVariable int examId) {
        // return all detail of an exam
        return BaseResponse.ok(null, "Exam retrieved successfully");
    }

    @GetMapping("/grade/{examId}")
    public BaseResponse<String> getExamGrade(@PathVariable int examId) {
        // return grade information of an exam: status, grade record,...
        return BaseResponse.ok(null, "Exam grade retrieved successfully");
    }

    @PostMapping("/create")
    public BaseResponse<String> createExam() {
        // create a new exam
        return BaseResponse.created(null, "Exam created successfully");
    }

    @PostMapping("/update-info")
    public BaseResponse<String> updateExamInfor() {
        // update an exam basic information
        return BaseResponse.accepted(null, "Exam info updated successfully");
    }

    @PostMapping("/update-status")
    public BaseResponse<String> updateExamStatus() {
        // update an exam status
        return BaseResponse.accepted(null, "Exam status updated successfully");
    }

    @PostMapping("/grade/{examId}")
    public BaseResponse<String> gradeExam(@PathVariable int examId) {
        // grade an exam
        // should handle all cases: first time & re-grade
        return BaseResponse.accepted(null, "Exam graded successfully");
    }

    @PostMapping("/cancel/{examId}")
    public BaseResponse<String> cancelExam(@PathVariable int examId) {
        // cancel an exam, should not delete it
        return BaseResponse.accepted(null, "Exam cancelled successfully");
    }
}
