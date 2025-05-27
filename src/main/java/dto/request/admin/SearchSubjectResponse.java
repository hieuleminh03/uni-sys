package dto.request.admin;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SearchSubjectResponse {
    private Long subjectId;
    private String subjectName;
    private String subjectCode;
}
