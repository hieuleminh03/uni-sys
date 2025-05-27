package dto.response.student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentAnnouncementDetailResponse {
    private Long id;
    private String title;
    private String content;
    private Date date;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String adminName;
}