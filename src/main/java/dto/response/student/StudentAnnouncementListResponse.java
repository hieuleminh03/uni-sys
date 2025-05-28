package dto.response.student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.AnnouncementTarget;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentAnnouncementListResponse {
    private Long id;
    private String title;
    private String contentPreview;
    private LocalDate date;
    private LocalDateTime createdAt;
    private String adminName;
}