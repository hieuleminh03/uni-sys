package controller.business.teacher;

import dto.request.teacher.TeacherAttendanceCreateRequest;
import dto.response.BaseResponse;
import dto.response.teacher.AttendanceListResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import service.teacher.TeacherAttendanceServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/teacher/attendance")
@RequiredArgsConstructor
@PreAuthorize("hasRole('TEACHER')")
public class TeacherAttendanceController {

    private final TeacherAttendanceServiceImpl teacherAttendanceService;
    @GetMapping("/byClass/{classId}")
    public BaseResponse<List<AttendanceListResponse>> getAttendanceByClass(@PathVariable Long classId) {

        return BaseResponse.ok(teacherAttendanceService.getAttendanceByClass(classId), "List of attendance for class " + classId + " retrieved successfully", null);
    }


    @PostMapping("/create/{classId}")
    public BaseResponse<String> createAttendance(@PathVariable Long classId,
                                                 @Valid  @RequestBody TeacherAttendanceCreateRequest teacherAttendanceCreateRequest) {
        teacherAttendanceService.createAttendance(classId, teacherAttendanceCreateRequest);
        return BaseResponse.created(null, "Attendance created successfully");
    }

    @PostMapping("/manual")
    public BaseResponse<String> manualAttendance() {
        // for manual only
        // should take in attendance id, student id as teacher call each student's name
        // available within the length of the roll call
        return BaseResponse.accepted(null, "Attendance updated successfully");
    }

    @PatchMapping("/update/{attendanceId}")
    public BaseResponse<String> updateAttendance() {
        // could use for any roll call type
        // for latecomers, students who are absent but have a reason
        return BaseResponse.accepted(null, "Attendance updated successfully");
    }

}
