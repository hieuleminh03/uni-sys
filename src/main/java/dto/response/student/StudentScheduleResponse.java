package dto.response.student;

import lombok.Builder;
import lombok.Getter;
import model.enums.DayOfWeek;

import java.util.Date;

@Getter
@Builder
public class StudentScheduleResponse {
    private Long classId;
    private String className;
    private String teacherName;
    private String teacherEmail;
    private Date startDate;
    private Date endDate;
    private String subjectName;
    private String subjectCode;
    private String room;
    private DayOfWeek dayOfWeek;
    private Integer periodStart;
    private Integer periodEnd;
}
