package controller.business.student;

import dto.response.BaseResponse;
import dto.response.student.StudentScheduleResponse;
import dto.response.teacher.TeacherScheduleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.student.StudentScheduleServiceImpl;
import service.teacher.TeacherScheduleServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/student/schedule")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
public class StudentScheduleController {
    private final StudentScheduleServiceImpl studentScheduleService;

    @GetMapping("")
    public BaseResponse<List<StudentScheduleResponse>> getScheduleByMonth(
            @RequestParam Integer month,
            @RequestParam Integer year) {
        return BaseResponse.ok(studentScheduleService.getScheduleByMonth(month , year),"Schedule retrieved successfully", null);
    }
}
