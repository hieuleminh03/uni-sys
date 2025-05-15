package controller.business.student;

import dto.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student/attendance")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
public class StudentAttendanceController {

    @GetMapping("/all")
    public BaseResponse<String> getAllAttendance() {
        // this could be called for the history screen
        return BaseResponse.ok(null, "List of all attendance retrieved successfully", null);
    }

    @GetMapping("/byClass/{classId}")
    public BaseResponse<String> getAttendanceByClass(@PathVariable String classId) {
        // return a list of existing roll call (id should be validated first)
        // should also return some meta data (class name, class id, anticipated/all, ...)
        return BaseResponse.ok(null, "List of attendance for class " + classId + " retrieved successfully", null);
    }

    @PostMapping("/qr")
    public BaseResponse<String> getAttendanceByQRCode() {
        // body should have attendance id as in the qr code
        return BaseResponse.ok(null, "Attendance retrieved successfully", null);
    }

}
