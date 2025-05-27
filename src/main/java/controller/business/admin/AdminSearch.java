package controller.business.admin;

import dto.request.admin.SearchSubjectResponse;
import dto.response.BaseResponse;
import dto.response.admin.SearchStudentResponse;
import dto.response.admin.SearchTeacherResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.admin.AdminSearchServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/admin/search")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@Tag(name = "Admin search", description = "Tìm kếm giáo viên hoặc học sinh")
public class AdminSearch {
    private final AdminSearchServiceImpl adminSearchService;
    @GetMapping("/teacher")
    public BaseResponse<List<SearchTeacherResponse>> searchTeacher(@RequestParam String query) {
        return BaseResponse.ok(adminSearchService.searchTeacher(query), " Tìm kiếm giáo viên thành công ");
    }

    @GetMapping("/student")
    public BaseResponse<List<SearchStudentResponse>> searchStudent(@RequestParam String query) {
        return BaseResponse.ok(adminSearchService.searchStudent(query), " Tìm kiếm học sinh thành công ");
    }

    @GetMapping("/subject")
    public BaseResponse<List<SearchSubjectResponse>> searchSubject(@RequestParam String query) {
        return BaseResponse.ok(adminSearchService.searchSubject(query), " Tìm kiếm môn học thành công ");
    }
}
