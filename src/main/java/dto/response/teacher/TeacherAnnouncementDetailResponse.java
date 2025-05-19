package dto.response.teacher;

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
public class TeacherAnnouncementDetailResponse {
    private Long id;
    private String title;
    private String content;
    private Date date;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String adminName;
    private String tags;
}