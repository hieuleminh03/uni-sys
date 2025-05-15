package controller.business.admin;

import dto.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/class")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminClassController {

    @GetMapping("/all")
    public BaseResponse<List<String>> getAllClasses() {
        // should be paging, have filter
        // url: /admin/class/all?page=1&size=10&active=true|false
        return BaseResponse.ok(List.of("Class 1", "Class 2"), "All classes retrieved successfully");
    }

    @PostMapping("/create")
    public BaseResponse<String> createClass() {
        // focus on data validation
        return BaseResponse.created(null, "Class created successfully");
    }

    @PatchMapping("/{id}/update")
    public BaseResponse<String> updateClass() {
        // class information
        return BaseResponse.accepted(null, "Class updated successfully");
    }

    /**
     * Class members
     */
    @GetMapping("/{id}/members")
    public BaseResponse<String> getClassMembers() {
        // class members: students, teacher
        return BaseResponse.ok(null, "Class members retrieved successfully");
    }

    @PostMapping("/{id}/students/add")
    public BaseResponse<String> addClassStudents() {
        // add new students to class
        return BaseResponse.created(null, "Students added to class successfully");
    }

    @PostMapping("/{id}/students/add/batch")
    public BaseResponse<String> addClassStudentsBatch() {
        // add new students to class
        return BaseResponse.created(null, "Students added to class successfully");
    }

    // remove students from class = update student status in class
    @PostMapping("/{id}/students/remove")
    public BaseResponse<String> removeClassStudents() {
        // remove students from class
        return BaseResponse.accepted(null, "Students removed from class successfully");
    }

    @PostMapping("/{id}/students/remove/batch")
    public BaseResponse<String> removeClassStudentsBatch() {
        // remove students from class
        return BaseResponse.accepted(null, "Students removed from class successfully");
    }

    @PatchMapping("/{id}/teacher/update")
    public BaseResponse<String> updateClassTeacher() {
        // handle all cases: new assignment, removal, change
        // send null as removal, another teacher id as change/new assignment
        return BaseResponse.accepted(null, "Class teacher updated successfully");
    }

    /**
     * Class schedule
     */
    @GetMapping("/{id}/schedule/all")
    public BaseResponse<String> getClassSchedule() {
        return BaseResponse.ok(null, "Class schedule retrieved successfully");
    }

    @PostMapping("/{id}/schedule/create")
    public BaseResponse<String> createClassSchedule() {
        return BaseResponse.created(null, "Class schedule created successfully");
    }

    @PatchMapping("/{id}/schedule/update")
    public BaseResponse<String> updateClassSchedule() {
        return BaseResponse.accepted(null, "Class schedule updated successfully");
    }

    @DeleteMapping("/{id}/schedule/delete")
    public BaseResponse<String> deleteClassSchedule() {
        return BaseResponse.accepted(null, "Class schedule deleted successfully");
    }
}
