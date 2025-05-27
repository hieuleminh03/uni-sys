package dto.response.teacher;

import lombok.Builder;
import lombok.Getter;
import model.enums.DayOfWeek;

import java.util.Date;

@Getter
@Builder
public class TeacherScheduleResponse {
    private Long classId;
    private String className;
    private Date startDate;
    private Date endDate;
    private String subjectName;
    private String subjectCode;
    private String room;
    private DayOfWeek dayOfWeek;
    private Integer periodStart;
    private Integer periodEnd;
}
