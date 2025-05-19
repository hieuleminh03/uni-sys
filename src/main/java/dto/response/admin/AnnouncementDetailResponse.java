package dto.response.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.AnnouncementTarget;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementDetailResponse {
    
    private Long id;
    private String title;
    private String content;
    private AnnouncementTarget target;
    private String targetName;
    private String tags;
    private Date date;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String adminName;
    private Long adminId;
}