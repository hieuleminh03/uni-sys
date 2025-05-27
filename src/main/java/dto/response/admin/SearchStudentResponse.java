package dto.response.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class SearchStudentResponse {
    private String fullName;
    private String avatarUrl;
    private String email;
    private Long studentId;

    public SearchStudentResponse(String fullName, String avatarUrl, String email, Long studentId) {
        this.fullName = fullName;
        this.avatarUrl = avatarUrl;
        this.email = email;
        this.studentId = studentId;
    }
}
