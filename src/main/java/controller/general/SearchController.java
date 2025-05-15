package controller.general;

import dto.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    /**
     * User search
     */
    @GetMapping("/user")
    public BaseResponse<List<String>> searchUser() {
        return BaseResponse.ok(null, "Search result");
    }

    /**
     * View user information (general information in users table)
     */
    @GetMapping("/user/view")
    public BaseResponse<String> viewInformation() {
        // like /search/view?userId=1
        return BaseResponse.ok(null, "View information");
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
