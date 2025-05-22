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
    private int teacherId;
    private String email;
    private String avatarUrl;

    public SearchTeacherResponse(String fullName, String avatarUrl, String email, int teacherId) {
        this.fullName = fullName;
        this.avatarUrl = avatarUrl;
        this.email = email;
        this.teacherId = teacherId;
    }
}
