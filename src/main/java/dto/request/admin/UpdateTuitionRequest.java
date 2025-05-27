package dto.request.admin;

import lombok.Getter;
import model.enums.TuitionStatus;

@Getter
public class UpdateTuitionRequest {
    private TuitionStatus status;
}
