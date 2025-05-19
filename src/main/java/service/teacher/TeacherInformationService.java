package service.teacher;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.request.teacher.TeacherUpdateInformationRequest;
import dto.response.BaseResponse;
import dto.response.admin.TeacherDetailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.Teacher;
import model.TeacherInformation;
import model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.TeacherInformationRepository;
import repository.TeacherRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherInformationService {

    private final TeacherRepository teacherRepository;
    private final TeacherInformationRepository teacherInformationRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Get the current teacher's information
     * @return Teacher information response
     */
    @Transactional(readOnly = true)
    public BaseResponse<TeacherDetailResponse> getTeacherInformation() {
        try {
            // Get current authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) authentication.getPrincipal();
            
            Teacher teacher = currentUser.getTeacher();
            if (teacher == null) {
                return BaseResponse.error(HttpStatus.NOT_FOUND.value(), "Teacher information not found", null, null);
            }
            
            // Get teacher information
            TeacherInformation teacherInfo = teacherInformationRepository.findByTeacher(teacher).orElse(null);
            
            // Build response
            TeacherDetailResponse.TeacherDetailResponseBuilder responseBuilder = TeacherDetailResponse.builder()
                    .id(teacher.getId())
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
            
            // Add teacher-specific information if available
            if (teacherInfo != null) {
                responseBuilder
                        .diplomaLevel(teacherInfo.getDiplomaLevel())
                        .careerDesc(teacherInfo.getCareerDesc());
            }
            
            return BaseResponse.ok(responseBuilder.build(), "Teacher information retrieved successfully");
        } catch (Exception e) {
            log.error("Failed to retrieve teacher information", e);
            return BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to retrieve teacher information", e, null);
        }
    }
    
    /**
     * Update the current teacher's information
     * @param request Teacher update information request
     * @return Success or error message response
     */
    @Transactional
    public BaseResponse<String> updateTeacherInformation(TeacherUpdateInformationRequest request) {
        try {
            // Get current authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) authentication.getPrincipal();
            
            Teacher teacher = currentUser.getTeacher();
            if (teacher == null) {
                return BaseResponse.error(HttpStatus.NOT_FOUND.value(), "Teacher information not found", null, null);
            }
            
            // Update teacher-specific information
            TeacherInformation teacherInfo = teacherInformationRepository.findByTeacher(teacher)
                    .orElse(TeacherInformation.builder().teacher(teacher).build());
            
            // Update allowed fields
            if (request.getDiplomaLevel() != null) {
                teacherInfo.setDiplomaLevel(request.getDiplomaLevel());
            }
            
            if (request.getCareerDesc() != null) {
                try {
                    // Check if the careerDesc is already a valid JSON string
                    try {
                        objectMapper.readTree(request.getCareerDesc());
                        // If no exception is thrown, it's already a valid JSON string
                        teacherInfo.setCareerDesc(request.getCareerDesc());
                    } catch (Exception e) {
                        // Not a valid JSON, convert the plain text to a JSON string
                        String jsonString = objectMapper.writeValueAsString(request.getCareerDesc());
                        teacherInfo.setCareerDesc(jsonString);
                    }
                } catch (Exception e) {
                    log.error("Invalid career description format", e);
                    return BaseResponse.error(HttpStatus.BAD_REQUEST.value(), "Invalid career description format: " + e.getMessage(), e, null);
                }
            }
            
            // Save the updated information
            teacherInformationRepository.save(teacherInfo);
            
            log.info("Updated teacher information for teacher ID: {}", teacher.getId());
            return BaseResponse.accepted(null, "Teacher information updated successfully");
        } catch (Exception e) {
            log.error("Failed to update teacher information", e);
            return BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to update teacher information", e, null);
        }
    }
}