package controller.general;

import dto.response.BaseResponse;
import dto.response.admin.SubjectListResponse;
import dto.response.search.UserDetailResponse;
import dto.response.search.UserSearchResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.search.SearchServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
@Tag(name = "Search", description = "Search is enabled for every roles")
public class SearchController {

    private final SearchServiceImpl searchService;

    /**
     * User search by username or email
     * @param query The search query (username or email)
     * @return List of matching users
     */
    @Operation(summary = "Search users", description = "Search users by username or email")
    @GetMapping("/user")
    public BaseResponse<List<UserSearchResponse>> searchUser(@RequestParam(required = false) String query) {
        List<UserSearchResponse> results = searchService.searchUser(query);
        return BaseResponse.ok(results, "Search result");
    }

    /**
     * View user information (general information in users table)
     * @param userId The user ID
     * @return User detail information
     */
    @Operation(summary = "View user information", description = "View user information by user ID")
    @GetMapping("/user/view")
    public BaseResponse<UserDetailResponse> viewInformation(@RequestParam Long userId) {
        UserDetailResponse userDetail = searchService.viewInformation(userId);
        return BaseResponse.ok(userDetail, "User information");
    }

    /**
     * Subject search by name or code
     * @param query The search query (name or code)
     * @return List of matching subjects
     */
    @Operation(summary = "Search subjects", description = "Search subjects by name or code")
    @GetMapping("/subject")
    public BaseResponse<List<SubjectListResponse>> searchSubject(@RequestParam(required = false) String query) {
        List<SubjectListResponse> results = searchService.searchSubject(query);
        return BaseResponse.ok(results, "Search result");
    }

    /**
     * Class search
     */
    @GetMapping("/class")
    public BaseResponse<List<String>> searchClass() {
        // with paging
        return BaseResponse.ok(null, "Search result");
    }

}
