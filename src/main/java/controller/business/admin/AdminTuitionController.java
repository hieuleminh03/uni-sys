package controller.business.admin;

import dto.request.admin.UpdateTuitionRequest;
import dto.response.BaseResponse;
import dto.response.admin.TuitionListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import service.admin.AdminTuitionServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/admin/tuition")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminTuitionController {

    private final AdminTuitionServiceImpl adminTuitionService;

    @GetMapping("/all")
    public BaseResponse<String> getAllTuition() {
        // return list of classes with tuition information
        // allows filtering by class, tuition status, tuition method
        return BaseResponse.ok(null, "List of all tuition retrieved successfully", null);
    }

    @GetMapping("/byClass/{classId}")
    public BaseResponse<String> getTuitionByClass(@PathVariable String classId) {
        // return a list of existing tuition (id should be validated first)
        // contain the tuition status of students in the class
        return BaseResponse.ok(null, "List of tuition for class " + classId + " retrieved successfully", null);
    }

    @PostMapping("/update-status")
    public BaseResponse<String> updateTuition() {
        // apply for cash / banking
        // when a student pays tuition, admin should update the tuition status
        return BaseResponse.accepted(null, "Tuition updated successfully");
    }

    @PostMapping("/update-method")
    public BaseResponse<String> updateTuitionMethod() {
        // update the tuition method (cash/banking/scholarship/financial aid -> between)
        return BaseResponse.accepted(null, "Tuition method updated successfully");
    }

    @GetMapping("/all/{studentId}")
    public ResponseEntity<BaseResponse<List<TuitionListResponse>>> getTuitionByStudent(@PathVariable Long studentId,
                                                                       @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(adminTuitionService.getTuitionListByStudent(studentId,page,size));
    }

    @GetMapping("/not-paid/{studentId}")
    public ResponseEntity<BaseResponse<List<TuitionListResponse>>> getNotPaidTuitionByStudent(@PathVariable Long studentId,
                                                                                 @RequestParam(defaultValue = "0") int page,
                                                                                 @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(adminTuitionService.getNotPaidTuitionListByStudent(studentId,page,size));
    }

    @PatchMapping("/update-status/{tuitionId}")
    public ResponseEntity<BaseResponse<String>> updateTuitionStatus(@PathVariable Long tuitionId, @RequestBody UpdateTuitionRequest updateTuitionRequest) {
        return ResponseEntity.ok(adminTuitionService.updateTuitionStatus(tuitionId,updateTuitionRequest.getStatus()));
    }
}
