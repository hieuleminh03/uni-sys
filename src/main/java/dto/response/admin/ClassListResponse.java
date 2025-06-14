package dto.response.admin;

import lombok.Builder;
import lombok.Getter;
import model.enums.DayOfWeek;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
@Getter
@Builder
public class ClassListResponse {
    private Long id;
    private String className;
    private Long teacherId;
    private String teacherFullName;
    private String teacherEmail;
    private String teacherAvatarUrl;
    private Long subjectId;
    private String subjectName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private BigDecimal tuition;
    private LocalDate tuitionDueDate;
    private Float finalTermWeight;
    private DayOfWeek dayOfWeek;
    private Integer periodStart;
    private Integer periodEnd;
    private Integer studentCount;
    private String room;
}
