package dto.request.teacher;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.DiplomaLevel;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherUpdateInformationRequest {
    private DiplomaLevel diplomaLevel;
    
    private String careerDesc;
}