package controller.business.admin;

import dto.request.admin.AddStudentToClassRequest;
import dto.request.admin.CreateClassRequest;
import dto.response.BaseResponse;
import dto.response.admin.ClassDetailResponse;
import dto.response.admin.ClassListResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import service.admin.AdminClassServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/admin/class")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminClassController {

    private final AdminClassServiceImpl adminClassService;

    @GetMapping("/all")
    @Operation(summary = "Get All class", description = "")
    public BaseResponse<List<ClassListResponse>> getAllClasses(@RequestParam(defaultValue = "1") int page,
                                                               @RequestParam(defaultValue = "10") int size){


        return adminClassService.getAllClasses(page,size);
    }

    @PostMapping("/create")
    @Operation(summary = "Create Class")
    public BaseResponse<String> createClass(@Valid  @RequestBody CreateClassRequest createClassRequest) {
        adminClassService.createClass(createClassRequest);
        return BaseResponse.created(null, "Class created successfully");
    }

    @PutMapping("/{id}")
    public BaseResponse<String> updateClass(@PathVariable Long id, @Valid @RequestBody CreateClassRequest updateClassRequest) {
        adminClassService.updateClass(id, updateClassRequest);
        return BaseResponse.accepted(null, "Class updated successfully");
    }
    @GetMapping("/{id}")
    @Operation(summary = "Get Class Details")
    public BaseResponse<ClassDetailResponse> getClassDetails(@PathVariable Long id) {

        return BaseResponse.ok(adminClassService.getClassDetailsById(id), "Class members retrieved successfully");
    }
    /**
     * Class members
     */


    @PostMapping("/{id}/students/add/batch")
    public BaseResponse<String> addClassStudentsBatch(@PathVariable Long id,
                                                      @RequestBody AddStudentToClassRequest studentIds) {
        adminClassService.addClassStudentsBatch(id, studentIds.getStudentIds());
        return BaseResponse.created(null, "Students added to class successfully");
    }

    // remove students from class = update student status in class
    @DeleteMapping("/{id}/students/remove")
    public BaseResponse<String> removeClassStudents(@PathVariable Long id){
        adminClassService.removeClassStudents(id);
        return BaseResponse.accepted(null, "Students removed from class successfully");
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
