package service.admin;

import dto.request.admin.AdminUpdateProfileRequest;
import dto.request.admin.UpdateStudentRequest;
import dto.request.admin.UpdateTeacherRequest;
import dto.response.BaseResponse;
import dto.response.admin.AdminDetailResponse;
import dto.response.admin.StudentDetailResponse;
import dto.response.admin.TeacherDetailResponse;
import dto.response.admin.UserListResponse;
import exception.BadRequestException;
import exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.Admin;
import model.Student;
import model.StudentInformation;
import model.Teacher;
import model.TeacherInformation;
import model.User;
import model.enums.Role;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.AdminRepository;
import repository.StudentInformationRepository;
import repository.StudentRepository;
import repository.TeacherInformationRepository;
import repository.TeacherRepository;
import repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminUserServiceImpl {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final StudentInformationRepository studentInformationRepository;
    private final TeacherInformationRepository teacherInformationRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Get all users in the system
     * @return List of all users with basic information
     */
    @Transactional(readOnly = true)
    public BaseResponse<List<UserListResponse>> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            
            List<UserListResponse> userResponses = users.stream()
                .map(user -> {
                    UserListResponse response = UserListResponse.builder()
                            .id(user.getId())
                            .username(user.getUsername())
                            .fullName(user.getFullName())
                            .role(user.getRole())
                            .gender(user.getGender())
                            .dob(user.getDob())
                            .email(user.getEmail())
                            .phoneNumber(user.getPhoneNumber())
                            .avatarUrl(user.getAvatarUrl())
                            .createdAt(user.getCreatedAt())
                            .updatedAt(user.getUpdatedAt())
                            .build();
                    
                    // Add role-specific IDs if available
                    if (user.getRole() == Role.ADMIN && user.getAdmin() != null) {
                        response.setAdminId(user.getAdmin().getId());
                    } else if (user.getRole() == Role.TEACHER && user.getTeacher() != null) {
                        response.setTeacherId(user.getTeacher().getId());
                    } else if (user.getRole() == Role.STUDENT && user.getStudent() != null) {
                        response.setStudentId(user.getStudent().getId());
                    }
                    
                    return response;
                })
                .collect(Collectors.toList());
            
            return BaseResponse.ok(userResponses, "List of all users retrieved successfully");
        } catch (Exception e) {
            log.error("Failed to retrieve user list", e);
            return BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to retrieve user list", e, null);
        }
    }

    /**
     * Get detailed admin information
     * @param adminId ID of the admin to retrieve
     * @return Detailed admin information
     */
    @Transactional(readOnly = true)
    public BaseResponse<AdminDetailResponse> getAdminDetail(Long adminId) {
        try {
            Admin admin = adminRepository.findById(adminId)
                    .orElseThrow(() -> new ResourceNotFoundException("Admin not found with ID: " + adminId));
            
            User user = admin.getUser();
            
            AdminDetailResponse response = AdminDetailResponse.builder()
                    .id(admin.getId())
                    .userId(user.getId())
                    .username(user.getUsername())
                    .fullName(user.getFullName())
                    .gender(user.getGender())
                    .dob(user.getDob())
                    .email(user.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .avatarUrl(user.getAvatarUrl())
                    .note(admin.getNote())
                    .createdAt(user.getCreatedAt())
                    .updatedAt(user.getUpdatedAt())
                    .build();
            
            return BaseResponse.ok(response, "Admin details retrieved successfully");
        } catch (ResourceNotFoundException e) {
            log.error("Admin not found", e);
            return BaseResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage(), e, null);
        } catch (Exception e) {
            log.error("Failed to retrieve admin details", e);
            return BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to retrieve admin details", e, null);
        }
    }
    
    /**
     * Get detailed student information
     * @param studentId ID of the student to retrieve
     * @return Detailed student information
     */
    @Transactional(readOnly = true)
    public BaseResponse<StudentDetailResponse> getStudentDetail(Long studentId) {
        try {
            Student student = studentRepository.findByIdWithUser(studentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));
            
            User user = student.getUser();
            StudentInformation studentInfo = studentInformationRepository.findByStudent(student)
                    .orElse(null);
            
            StudentDetailResponse.StudentDetailResponseBuilder responseBuilder = StudentDetailResponse.builder()
                    .id(student.getId())
                    .userId(user.getId())
                    .username(user.getUsername())
                    .fullName(user.getFullName())
                    .gender(user.getGender())
                    .dob(user.getDob())
                    .email(user.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .avatarUrl(user.getAvatarUrl())
                    .createdAt(user.getCreatedAt())
                    .updatedAt(user.getUpdatedAt());
            
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
            
            return BaseResponse.ok(responseBuilder.build(), "Student details retrieved successfully");
        } catch (ResourceNotFoundException e) {
            log.error("Student not found", e);
            return BaseResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage(), e, null);
        } catch (Exception e) {
            log.error("Failed to retrieve student details", e);
            return BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to retrieve student details", e, null);
        }
    }
    
    /**
     * Get detailed teacher information
     * @param teacherId ID of the teacher to retrieve
     * @return Detailed teacher information
     */
    @Transactional(readOnly = true)
    public BaseResponse<TeacherDetailResponse> getTeacherDetail(Long teacherId) {
        try {
            Teacher teacher = teacherRepository.findByIdWithUser(teacherId)
                    .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with ID: " + teacherId));
            
            User user = teacher.getUser();
            TeacherInformation teacherInfo = teacherInformationRepository.findByTeacher(teacher)
                    .orElse(null);
            
            TeacherDetailResponse.TeacherDetailResponseBuilder responseBuilder = TeacherDetailResponse.builder()
                    .id(teacher.getId())
                    .userId(user.getId())
                    .username(user.getUsername())
                    .fullName(user.getFullName())
                    .gender(user.getGender())
                    .dob(user.getDob())
                    .email(user.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .avatarUrl(user.getAvatarUrl())
                    .createdAt(user.getCreatedAt())
                    .updatedAt(user.getUpdatedAt());
            
            // Add teacher-specific information if available
            if (teacherInfo != null) {
                responseBuilder
                        .diplomaLevel(teacherInfo.getDiplomaLevel())
                        .careerDesc(teacherInfo.getCareerDesc());
            }
            
            return BaseResponse.ok(responseBuilder.build(), "Teacher details retrieved successfully");
        } catch (ResourceNotFoundException e) {
            log.error("Teacher not found", e);
            return BaseResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage(), e, null);
        } catch (Exception e) {
            log.error("Failed to retrieve teacher details", e);
            return BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to retrieve teacher details", e, null);
        }
    }

    /**
     * Update student information
     * @param request UpdateStudentRequest containing the student information to update
     * @return Success or error message
     */
    @Transactional
    public BaseResponse<String> updateStudentInformation(UpdateStudentRequest request) {
        try {
            Student student = studentRepository.findByIdWithUser(request.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + request.getStudentId()));
            
            User user = student.getUser();
            
            // Update base user information
            if (request.getFullName() != null && !request.getFullName().isBlank()) {
                user.setFullName(request.getFullName());
            }
            if (request.getDob() != null) {
                user.setDob(request.getDob());
            }
            if (request.getGender() != null) {
                user.setGender(request.getGender());
            }
            if (request.getPhoneNumber() != null && !request.getPhoneNumber().isBlank()) {
                user.setPhoneNumber(request.getPhoneNumber());
            }
            if (request.getEmail() != null && !request.getEmail().isBlank()) {
                // Check if email is already in use by another user
                List<User> usersWithEmail = userRepository.findAll().stream()
                        .filter(u -> request.getEmail().equals(u.getEmail()) && !u.getId().equals(user.getId()))
                        .toList();
                
                if (!usersWithEmail.isEmpty()) {
                    throw new BadRequestException("Email is already in use by another user");
                }
                
                user.setEmail(request.getEmail());
            }
            if (request.getAvatarUrl() != null) {
                user.setAvatarUrl(request.getAvatarUrl());
            }
            
            // Save the updated user information
            userRepository.save(user);
            
            // Update student-specific information
            StudentInformation studentInfo = studentInformationRepository.findByStudent(student)
                    .orElse(StudentInformation.builder().student(student).build());
            
            if (request.getEthnicity() != null) {
                studentInfo.setEthicity(request.getEthnicity());
            }
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
            if (request.getReligion() != null) {
                studentInfo.setReligion(request.getReligion());
            }
            if (request.getMotherName() != null) {
                studentInfo.setMotherName(request.getMotherName());
            }
            if (request.getMotherYob() != null) {
                studentInfo.setMotherYob(request.getMotherYob());
            }
            if (request.getMotherPhone() != null) {
                studentInfo.setMotherPhone(request.getMotherPhone());
            }
            if (request.getMotherMail() != null) {
                studentInfo.setMotherMail(request.getMotherMail());
            }
            if (request.getMotherOccupation() != null) {
                studentInfo.setMotherOccupation(request.getMotherOccupation());
            }
            if (request.getFatherName() != null) {
                studentInfo.setFatherName(request.getFatherName());
            }
            if (request.getFatherYob() != null) {
                studentInfo.setFatherYob(request.getFatherYob());
            }
            if (request.getFatherPhone() != null) {
                studentInfo.setFatherPhone(request.getFatherPhone());
            }
            if (request.getFatherMail() != null) {
                studentInfo.setFatherMail(request.getFatherMail());
            }
            if (request.getFatherOccupation() != null) {
                studentInfo.setFatherOccupation(request.getFatherOccupation());
            }
            
            studentInformationRepository.save(studentInfo);
            
            log.info("Updated student information for student ID: {}", request.getStudentId());
            return BaseResponse.accepted(null, "Student information updated successfully");
        } catch (ResourceNotFoundException e) {
            log.error("Student not found during update", e);
            return BaseResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage(), e, null);
        } catch (BadRequestException e) {
            log.error("Bad request during student update", e);
            return BaseResponse.badRequest(e.getMessage(), e, null);
        } catch (Exception e) {
            log.error("Failed to update student information", e);
            return BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to update student information", e, null);
        }
    }
    
    /**
     * Update teacher information
     * @param request UpdateTeacherRequest containing the teacher information to update
     * @return Success or error message
     */
    @Transactional
    public BaseResponse<String> updateTeacherInformation(UpdateTeacherRequest request) {
        try {
            Teacher teacher = teacherRepository.findByIdWithUser(request.getTeacherId())
                    .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with ID: " + request.getTeacherId()));
            
            User user = teacher.getUser();
            
            // Update base user information
            if (request.getFullName() != null && !request.getFullName().isBlank()) {
                user.setFullName(request.getFullName());
            }
            if (request.getDob() != null) {
                user.setDob(request.getDob());
            }
            if (request.getGender() != null) {
                user.setGender(request.getGender());
            }
            if (request.getPhoneNumber() != null && !request.getPhoneNumber().isBlank()) {
                user.setPhoneNumber(request.getPhoneNumber());
            }
            if (request.getEmail() != null && !request.getEmail().isBlank()) {
                // Check if email is already in use by another user
                List<User> usersWithEmail = userRepository.findAll().stream()
                        .filter(u -> request.getEmail().equals(u.getEmail()) && !u.getId().equals(user.getId()))
                        .toList();
                
                if (!usersWithEmail.isEmpty()) {
                    throw new BadRequestException("Email is already in use by another user");
                }
                
                user.setEmail(request.getEmail());
            }
            if (request.getAvatarUrl() != null) {
                user.setAvatarUrl(request.getAvatarUrl());
            }
            
            // Save the updated user information
            userRepository.save(user);
            
            // Update teacher-specific information
            TeacherInformation teacherInfo = teacherInformationRepository.findByTeacher(teacher)
                    .orElse(TeacherInformation.builder().teacher(teacher).build());
            
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
                        // Wrap the career description in quotes to make it a valid JSON string
                        String jsonString = objectMapper.writeValueAsString(request.getCareerDesc());
                        teacherInfo.setCareerDesc(jsonString);
                    }
                } catch (Exception e) {
                    throw new BadRequestException("Invalid career description format: " + e.getMessage());
                }
            }
            
            teacherInformationRepository.save(teacherInfo);
            
            log.info("Updated teacher information for teacher ID: {}", request.getTeacherId());
            return BaseResponse.accepted(null, "Teacher information updated successfully");
        } catch (ResourceNotFoundException e) {
            log.error("Teacher not found during update", e);
            return BaseResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage(), e, null);
        } catch (BadRequestException e) {
            log.error("Bad request during teacher update", e);
            return BaseResponse.badRequest(e.getMessage(), e, null);
        } catch (Exception e) {
            log.error("Failed to update teacher information", e);
            return BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to update teacher information", e, null);
        }
    }
    
    /**
     * Update admin profile
     * @param request AdminUpdateProfileRequest containing the admin information to update
     * @return Success or error message
     */
    @Transactional
    public BaseResponse<String> updateAdminProfile(AdminUpdateProfileRequest request) {
        try {
            Admin admin = adminRepository.findById(request.getAdminId())
                    .orElseThrow(() -> new ResourceNotFoundException("Admin not found with ID: " + request.getAdminId()));
            
            User user = admin.getUser();
            
            // Update base user information
            if (request.getFullName() != null && !request.getFullName().isBlank()) {
                user.setFullName(request.getFullName());
            }
            if (request.getDob() != null) {
                user.setDob(request.getDob());
            }
            if (request.getGender() != null) {
                user.setGender(request.getGender());
            }
            if (request.getPhoneNumber() != null && !request.getPhoneNumber().isBlank()) {
                user.setPhoneNumber(request.getPhoneNumber());
            }
            if (request.getEmail() != null && !request.getEmail().isBlank()) {
                // Check if email is already in use by another user
                List<User> usersWithEmail = userRepository.findAll().stream()
                        .filter(u -> request.getEmail().equals(u.getEmail()) && !u.getId().equals(user.getId()))
                        .toList();
                
                if (!usersWithEmail.isEmpty()) {
                    throw new BadRequestException("Email is already in use by another user");
                }
                
                user.setEmail(request.getEmail());
            }
            if (request.getAvatarUrl() != null) {
                user.setAvatarUrl(request.getAvatarUrl());
            }
            
            // Save the updated user information
            userRepository.save(user);
            
            // Update admin-specific information
            if (request.getNote() != null) {
                try {
                    // Check if the note is already a valid JSON string
                    try {
                        objectMapper.readTree(request.getNote());
                        // If no exception is thrown, it's already a valid JSON string
                        admin.setNote(request.getNote());
                    } catch (Exception e) {
                        // Not a valid JSON, convert the plain text to a JSON string
                        String jsonString = objectMapper.writeValueAsString(request.getNote());
                        admin.setNote(jsonString);
                    }
                } catch (Exception e) {
                    throw new BadRequestException("Invalid note format: " + e.getMessage());
                }
            }
            
            adminRepository.save(admin);
            
            log.info("Updated admin profile for admin ID: {}", request.getAdminId());
            return BaseResponse.accepted(null, "Admin profile updated successfully");
        } catch (ResourceNotFoundException e) {
            log.error("Admin not found during update", e);
            return BaseResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage(), e, null);
        } catch (BadRequestException e) {
            log.error("Bad request during admin update", e);
            return BaseResponse.badRequest(e.getMessage(), e, null);
        } catch (Exception e) {
            log.error("Failed to update admin profile", e);
            return BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to update admin profile", e, null);
        }
    }
}
