package service.student;

import dto.request.student.StudentUpdateInformationRequest;
import dto.response.BaseResponse;
import dto.response.admin.StudentDetailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.Student;
import model.StudentInformation;
import model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.StudentInformationRepository;
import repository.StudentRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentInformationService {

    private final StudentRepository studentRepository;
    private final StudentInformationRepository studentInformationRepository;

    /**
     * Get the current student's information
     * @return Student information response
     */
    @Transactional(readOnly = true)
    public BaseResponse<StudentDetailResponse> getStudentInformation() {
        try {
            // Get current authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) authentication.getPrincipal();
            
            Student student = currentUser.getStudent();
            if (student == null) {
                return BaseResponse.error(HttpStatus.NOT_FOUND.value(), "Student information not found", null, null);
            }
            
            // Get student information
            StudentInformation studentInfo = studentInformationRepository.findByStudent(student).orElse(null);
            
            // Build response
            StudentDetailResponse.StudentDetailResponseBuilder responseBuilder = StudentDetailResponse.builder()
                    .id(student.getId())
                    .userId(currentUser.getId())
                    .username(currentUser.getUsername())
                    .fullName(currentUser.getFullName())
                    .gender(currentUser.getGender())
                    .dob(currentUser.getDob())
                    .email(currentUser.getEmail())
                    .phoneNumber(currentUser.getPhoneNumber())
                    .avatarUrl(currentUser.getAvatarUrl())
                    .createdAt(currentUser.getCreatedAt())
                    .updatedAt(currentUser.getUpdatedAt());
            
            // Add student-specific information if available
            if (studentInfo != null) {
                responseBuilder
                        .ethnicity(studentInfo.getEthicity())
                        .idCardNumber(studentInfo.getIdCardNumber())
                        .idCardPlaceOfIssue(studentInfo.getIdCardPlaceOfIssue())
                        .residence(studentInfo.getResidence())
                        .address(studentInfo.getAddress())
                        .religion(studentInfo.getReligion())
                        .motherName(studentInfo.getMotherName())
                        .motherYob(studentInfo.getMotherYob())
                        .motherPhone(studentInfo.getMotherPhone())
                        .motherMail(studentInfo.getMotherMail())
                        .motherOccupation(studentInfo.getMotherOccupation())
                        .fatherName(studentInfo.getFatherName())
                        .fatherYob(studentInfo.getFatherYob())
                        .fatherPhone(studentInfo.getFatherPhone())
                        .fatherMail(studentInfo.getFatherMail())
                        .fatherOccupation(studentInfo.getFatherOccupation());
            }
            
            return BaseResponse.ok(responseBuilder.build(), "Student information retrieved successfully");
        } catch (Exception e) {
            log.error("Failed to retrieve student information", e);
            return BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to retrieve student information", e, null);
        }
    }
    
    /**
     * Update the current student's information
     * @param request Student update information request
     * @return Success or error message response
     */
    @Transactional
    public BaseResponse<String> updateStudentInformation(StudentUpdateInformationRequest request) {
        try {
            // Get current authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) authentication.getPrincipal();
            
            Student student = currentUser.getStudent();
            if (student == null) {
                return BaseResponse.error(HttpStatus.NOT_FOUND.value(), "Student information not found", null, null);
            }
            
            // Update student-specific information
            StudentInformation studentInfo = studentInformationRepository.findByStudent(student)
                    .orElse(StudentInformation.builder().student(student).build());
            
            // Update allowed fields
            if (request.getIdCardNumber() != null) {
                studentInfo.setIdCardNumber(request.getIdCardNumber());
            }
            if (request.getIdCardPlaceOfIssue() != null) {
                studentInfo.setIdCardPlaceOfIssue(request.getIdCardPlaceOfIssue());
            }
            if (request.getResidence() != null) {
                studentInfo.setResidence(request.getResidence());
            }
            if (request.getAddress() != null) {
                studentInfo.setAddress(request.getAddress());
            }
            if (request.getMotherPhone() != null) {
                studentInfo.setMotherPhone(request.getMotherPhone());
            }
            if (request.getMotherMail() != null) {
                studentInfo.setMotherMail(request.getMotherMail());
            }
            if (request.getFatherPhone() != null) {
                studentInfo.setFatherPhone(request.getFatherPhone());
            }
            if (request.getFatherMail() != null) {
                studentInfo.setFatherMail(request.getFatherMail());
            }
            
            // Save the updated information
            studentInformationRepository.save(studentInfo);
            
            log.info("Updated student information for student ID: {}", student.getId());
            return BaseResponse.accepted(null, "Student information updated successfully");
        } catch (Exception e) {
            log.error("Failed to update student information", e);
            return BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to update student information", e, null);
        }
    }
}