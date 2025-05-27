package dto.response.admin;

import lombok.Builder;
import lombok.Getter;
import model.enums.DayOfWeek;
import model.enums.TuitionStatus;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Builder
public class ClassDetailResponse {
    private Long id;
    private String className;
    private Long teacherId;
    private String teacherFullName;
    private String teacherEmail;
    private String teacherAvatarUrl;
    private Long subjectId;
    private String subjectCode;
    private String subjectName;
    private Date startDate;
    private Date endDate;
    private String description;
    private BigDecimal tuition;
    private Date tuitionDueDate;
    private Float finalTermWeight;
    private DayOfWeek dayOfWeek;
    private Integer periodStart;
    private Integer periodEnd;
    private Integer studentCount;
    private String room;
    private Integer absenceWarningThreshold;
    private Integer absenceLimit;
    private List<ClassStudentResponse> classStudentResponses;

    @Getter
    @Builder
    public static class ClassStudentResponse {
        private Long id;
        private String fullName;
        private String email;
        private String avatarUrl;
        private Integer absenceCount;
        private TuitionStatus tuitionStatus;
        private Double midtermGrade;
        private Boolean isAbsentMidterm;
        private Double finalTermGrade;
        private Boolean isAbsentFinalTerm;
    }
}
