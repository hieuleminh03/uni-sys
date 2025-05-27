package dto.request.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import model.enums.DayOfWeek;

import java.math.BigDecimal;
import java.util.Date;

@Getter
public class CreateClassRequest {
    @NotBlank(message = "Teacher ID is required")
    private Long teacherId;
    @NotBlank(message = "Subject ID is required")
    private Long subjectId;
    @NotBlank(message = "Class name is required")
    private String className;
    @NotBlank(message = "StartDate is required")
    private Date startDate;
    @NotBlank(message = "EndDate date is required")
    private Date endDate;
    private String description;
    @NotBlank(message = "Tuition is required")
    private BigDecimal tuition;

    @NotBlank(message = "Tuition due date is required")
    private Date tuitionDueDate;

    @NotBlank(message = "Final term weight is required")
    private Float finalTermWeight;

    @NotBlank(message = "Absence warning threshold is required")
    private Integer absenceWarningThreshold;

    @NotBlank(message = "Absence limit is required")
    private Integer absenceLimit;

    @NotBlank(message = "Class start time is required")
    private DayOfWeek dayOfWeek;

    @NotBlank(message = "Class start time is required")
    private Integer periodStart;

    @NotBlank(message = "Class end time is required")
    private Integer periodEnd;

    @NotBlank(message = "Class room is required")
    private String room;
}
