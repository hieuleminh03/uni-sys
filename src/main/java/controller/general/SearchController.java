package controller.general;

import dto.response.BaseResponse;
import dto.response.search.UserDetailResponse;
import dto.response.search.UserSearchResponse;
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
public class SearchController {

    private final SearchServiceImpl searchService;

    /**
     * User search by username or email
     * @param query The search query (username or email)
     * @return List of matching users
     */
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
    @GetMapping("/user/view")
    public BaseResponse<UserDetailResponse> viewInformation(@RequestParam Long userId) {
        UserDetailResponse userDetail = searchService.viewInformation(userId);
        return BaseResponse.ok(userDetail, "User information");
    }

    /**
     * Subject search
     */
    @GetMapping("/subject")
    public BaseResponse<List<String>> searchSubject() {
        // no paging
        return BaseResponse.ok(null, "Search result");
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
