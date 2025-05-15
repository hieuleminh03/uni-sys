package controller.business.admin;

import dto.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/subject")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminSubjectController {

    @GetMapping("/all")
    public BaseResponse<String> getAllSubjects() {
        return BaseResponse.ok(null, "All subjects retrieved successfully");
    }

    @GetMapping("/{id}")
    public BaseResponse<String> getSubjectById(@PathVariable int id) {
        // listing class with this subject & other information
        return BaseResponse.ok(null, "Subject information retrieved successfully");
    }

    @PostMapping("/create")
    public BaseResponse<String> createSubject() {
        return BaseResponse.created(null, "Subject created successfully");
    }

    @PatchMapping("/update")
    public BaseResponse<String> updateSubject() {
        return BaseResponse.accepted(null, "Subject updated successfully");
    }

}
