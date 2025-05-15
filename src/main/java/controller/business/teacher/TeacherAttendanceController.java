package controller.business.teacher;

import dto.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teacher/attendance")
@RequiredArgsConstructor
@PreAuthorize("hasRole('TEACHER')")
public class TeacherAttendanceController {

    @GetMapping("/all")
    public BaseResponse<String> getAllAttendance() {
        // this should not be called at all
        return BaseResponse.ok(null, "List of all attendance retrieved successfully", null);
    }

    @GetMapping("/byClass/{classId}")
    public BaseResponse<String> getAttendanceByClass(String classId) {
        // return a list of existing roll call
        // should also return some meta data (class name, class id, anticipated/all, ...)
        return BaseResponse.ok(null, "List of attendance for class " + classId + " retrieved successfully", null);
    }

    @GetMapping("/detail/{attendanceId}")
    public BaseResponse<String> getAttendance(String attendanceId) {
        // return all detail of an attendance (list of students, attendance status, reasons, ...)
        return BaseResponse.ok(null, "Attendance retrieved successfully");
    }

    @PostMapping("/create")
    public BaseResponse<String> createAttendance() {
        return BaseResponse.created(null, "Attendance created successfully");
    }

    @PostMapping("/manual")
    public BaseResponse<String> manualAttendance() {
        // for manual only
        // should take in attendance id, student id as teacher call each student's name
        // available within the length of the roll call
        return BaseResponse.accepted(null, "Attendance updated successfully");
    }

    @PatchMapping("/update")
    public BaseResponse<String> updateAttendance() {
        // could use for any roll call type
        // for latecomers, students who are absent but have a reason
        return BaseResponse.accepted(null, "Attendance updated successfully");
    }

}
