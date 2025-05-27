package controller.business.admin;

import dto.request.admin.AdminUpdateGradeRequest;
import dto.request.admin.CreateExamToClassRequest;
import dto.response.BaseResponse;
import dto.response.admin.ExamListResponse;
import dto.response.admin.ScheduleExamListResponse;
import lombok.RequiredArgsConstructor;
import model.enums.ExaminationType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import service.admin.AdminExamServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/admin/exam")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminExamController {

    private final AdminExamServiceImpl adminExamService;

    @GetMapping("/byClass/{classId}")
    public BaseResponse<ExamListResponse> getExamsByClass(@PathVariable Long classId,
                                                          @RequestParam (defaultValue ="MIDTERM") ExaminationType examinationType) {
        // return a list of general information (in a class)
        return BaseResponse.ok(adminExamService.getExamByClass(classId,examinationType), examinationType+ "exams for class " + classId + " retrieved successfully", null);
    }


    @PostMapping("/create/{classId}")
    public BaseResponse<String> createExam(@PathVariable Long classId,
                                           @RequestBody CreateExamToClassRequest createExamToClassRequest) {
        adminExamService.createExam(classId, createExamToClassRequest);
        return BaseResponse.created(null, "Exam created successfully");
    }
    @DeleteMapping("{examinationId}")
    public BaseResponse<String> deleteExam(@PathVariable Long examinationId) {
        adminExamService.deleteExam(examinationId);
        return BaseResponse.accepted(null, "Exam deleted successfully");
    }

    @PostMapping("/update-grade")
    public BaseResponse<String> updateGrade(@RequestBody AdminUpdateGradeRequest adminUpdateGradeRequests) {
        adminExamService.updateExam(adminUpdateGradeRequests);
        return BaseResponse.accepted(null, "Exam graded successfully");
    }

    @GetMapping("/schedule-exam")
    public ResponseEntity<BaseResponse<List<ScheduleExamListResponse>>> scheduleExam(@RequestParam (defaultValue = "0") int page,
                                                                                     @RequestParam (defaultValue = "10") int size){

        return ResponseEntity.ok(adminExamService.scheduleExam(page , size));
    }
}
