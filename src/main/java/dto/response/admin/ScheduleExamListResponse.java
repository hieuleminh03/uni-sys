package dto.response.admin;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import model.enums.ExaminationType;

import java.util.Date;
import java.util.List;

@Builder
@Getter
public class ScheduleExamListResponse {
    private Long classId;
    private String className;
    private List<ClassScheduleResponse> classScheduleResponses;


    @Getter
    @Builder
    public static class ClassScheduleResponse {
        private Long examinationId;
        private ExaminationType examinationType;
        private Date date;
    }
}
