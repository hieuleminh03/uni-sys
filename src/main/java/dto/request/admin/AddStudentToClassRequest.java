package dto.request.admin;

import lombok.Getter;

import java.util.List;

@Getter
public class AddStudentToClassRequest {
    private Long id;
    private List<Long> studentIds;
}
