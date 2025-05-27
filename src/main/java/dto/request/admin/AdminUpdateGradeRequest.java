package dto.request.admin;

import lombok.Getter;

import java.util.List;

@Getter
public class AdminUpdateGradeRequest {
    private List<UpdateGradeRequest> updateGradeRequests;

    @Getter
    public static class UpdateGradeRequest {
        private Long studentExaminationId;
        private Double grade;
    }

}
