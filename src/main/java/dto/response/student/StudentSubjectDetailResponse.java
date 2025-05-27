package dto.response.student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentSubjectDetailResponse {
    
    private Long id;
    private String name;
    private String code;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int totalClasses;
    private List<ClassInfo> classes;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClassInfo {
        private Long id;
        private String name;
        private String teacherEmail;
        private String teacherAvatarUrl;
        private String teacherName;
        private int totalStudents;
        private boolean enrolled;
        private Date startDate;
        private Date endDate;
    }
}