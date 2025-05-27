package dto.response.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class SearchTeacherResponse {
    private String fullName;
    private String avatarUrl;
    private String email;
    private Long teacherId;

    public SearchTeacherResponse(String fullName, String avatarUrl, String email, Long teacherId) {
        this.fullName = fullName;
        this.avatarUrl = avatarUrl;
        this.email = email;
        this.teacherId = teacherId;
    }
}
