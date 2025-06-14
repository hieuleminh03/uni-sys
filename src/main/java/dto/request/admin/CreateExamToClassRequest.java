package dto.request.admin;

import lombok.Getter;
import model.enums.ExaminationType;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
public class CreateExamToClassRequest {
    private ExaminationType examinationType;
    private LocalDate date;
    private List<ExamStudentRequest> examStudentRequests;

    @Getter
    public static class ExamStudentRequest {
        private Long classStudentId;
        private Boolean isAbsent;
    }
}
