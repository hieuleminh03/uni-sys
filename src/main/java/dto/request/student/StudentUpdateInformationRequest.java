package dto.request.student;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentUpdateInformationRequest {
    private String idCardNumber;
    
    private String idCardPlaceOfIssue;
    
    private String residence;
    
    private String address;
    
    @Pattern(regexp = "^\\+?[0-9]*$", message = "Mother phone number must contain only digits and optional '+' prefix")
    private String motherPhone;
    
    @Email(message = "Mother email must be a valid email address")
    private String motherMail;
    
    @Pattern(regexp = "^\\+?[0-9]*$", message = "Father phone number must contain only digits and optional '+' prefix")
    private String fatherPhone;
    
    @Email(message = "Father email must be a valid email address")
    private String fatherMail;
}