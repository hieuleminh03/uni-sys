package dto.response.admin;

import lombok.Builder;
import lombok.Getter;
import model.enums.TuitionStatus;

import java.math.BigDecimal;

@Builder
@Getter
public class TuitionListResponse {
    private Long id;
    private String className;
    private BigDecimal tuition;
    private TuitionStatus tuitionStatus;
}
