package controller.business.admin;

import dto.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/attendance")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminAttendanceController {

    /**
     * Desired flow: from a list of class, select a class -> show all attendance
     */

    @GetMapping("/all")
    public BaseResponse<String> getAllAttendance() {
        // this is not a good idea
        return BaseResponse.ok(null, "List of all attendance retrieved successfully", null);
    }

    @GetMapping("/byClass/{classId}")
    public BaseResponse<String> getAttendanceByClass(@PathVariable String classId) {
        // return a list of existing roll call
        // should also return some meta data (class name, class id, anticipated/all, ...)
        return BaseResponse.ok(null, "List of attendance for class " + classId + " retrieved successfully", null);
    }

    @GetMapping("/detail/{attendanceId}")
    public BaseResponse<String> getAttendance(@PathVariable String attendanceId) {
        // return all detail of an attendance (list of students, attendance status, reasons, ...)
        return BaseResponse.ok(null, "Attendance retrieved successfully");
    }
}
