package dto.response.student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.HomeroomStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentHomeroomClassmateResponse {
    private Long id;
    private Long studentId;
    private String studentName;
    private String studentAvatarUrl;
    private String studentEmail;
    private String studentCode;
    private HomeroomStatus status;
    private String statusName;
}