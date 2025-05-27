package dto.response.student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.TuitionRecord;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentGradeResponse {
    private Long classId;
    private String className;
    private Double midtermGrade;
    private Double finalExamGrade;
    private Float finalTermWeight;
    private Double finalGrade;

}
