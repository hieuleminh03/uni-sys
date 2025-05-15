package controller.business.student;

import dto.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student/tuition")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
public class StudentTuitionController {

    // tuition is 1-1 with class, therefore we dont need to split into 2 screens
    // should be a list of class, and tuition information in each class

    @GetMapping("/all")
    public BaseResponse<String> getAllTuition() {
        // this should be for listing screen
        // allows filtering by tuition type, tuition status (class should be the one that student is/was in)
        return BaseResponse.ok(null, "List of all tuition retrieved successfully", null);
    }

}
