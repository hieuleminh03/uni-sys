package controller.business.teacher;

import dto.response.BaseResponse;
import dto.response.teacher.TeacherScheduleResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.teacher.TeacherScheduleServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/teacher/schedule")
@RequiredArgsConstructor
@PreAuthorize("hasRole('TEACHER')")
public class TeacherScheduleController {
    private final TeacherScheduleServiceImpl teacherScheduleService;

    @GetMapping("")
    public BaseResponse<List<TeacherScheduleResponse>> getScheduleByMonth(
            @RequestParam Integer month,
            @RequestParam Integer year) {
        return BaseResponse.ok(teacherScheduleService.getScheduleByMonth(month , year),"Schedule retrieved successfully", null);
    }
}
