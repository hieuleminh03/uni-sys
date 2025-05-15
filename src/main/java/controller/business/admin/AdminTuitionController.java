package controller.business.admin;

import dto.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/tuition")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminTuitionController {

    // tuition itself does not have its own table
    // this is for managing the tuition history

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
}
