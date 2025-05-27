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
public class AnnouncementListResponse {
    
    private Long id;
    private String title;
    private String contentPreview;
    private AnnouncementTarget target;
    private String targetName;
    private Date date;
    private String adminName;
}