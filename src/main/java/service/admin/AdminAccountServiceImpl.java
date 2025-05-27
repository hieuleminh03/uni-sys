package service.admin;

import dto.request.admin.BatchCreateAccountRequest;
import dto.request.admin.ChangePasswordRequest;
import dto.request.admin.CreateAccountRequest;
import dto.request.admin.ResetPasswordRequest;
import dto.response.BaseResponse;
import dto.response.admin.AccountListResponse;
import dto.response.shared.AccountInformationResponse;
import exception.AuthenticationException;
import exception.BadRequestException;
import exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.Account;
import model.Admin;
import model.Student;
import model.StudentInformation;
import model.Teacher;
import model.TeacherInformation;
import model.User;
import model.enums.AccountStatus;
import model.enums.DiplomaLevel;
import model.enums.Role;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.AccountRepository;
import repository.AdminRepository;
import repository.StudentInformationRepository;
import repository.StudentRepository;
import repository.TeacherInformationRepository;
import repository.TeacherRepository;
import repository.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminAccountServiceImpl {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final StudentInformationRepository studentInformationRepository;
    private final TeacherInformationRepository teacherInformationRepository;
    private final PasswordEncoder passwordEncoder;

    public BaseResponse<AccountInformationResponse> getAccountInformation() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Account account = accountRepository.findByUsername(auth.getName())
                    .orElseThrow(() -> new RuntimeException("Account not found"));
            User user = userRepository.findByAccount(account).orElseThrow(
                    () -> new RuntimeException("User not found")
            );
            AccountInformationResponse response = AccountInformationResponse.builder()
                    .username(account.getUsername())
                    .status(account.getStatus())
                    .avatarUrl(user.getAvatarUrl())
                    .email(user.getEmail())
                    .fullName(user.getFullName())
                    .build();
            return BaseResponse.ok(response,"Account information retrieved successfully");
        } catch (Exception e) {
            return BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to retrieve account information", e, null);
        }
    }
    
    @Transactional
    public BaseResponse<String> changePassword(ChangePasswordRequest request) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Account account = accountRepository.findByUsername(auth.getName())
                    .orElseThrow(() -> new RuntimeException("Account not found"));
            
            // Verify current password
            if (!passwordEncoder.matches(request.getCurrentPassword(), account.getPassword())) {
                throw new AuthenticationException("Current password is incorrect");
            }
            
            // Validate new password
            if (request.getNewPassword() == null || request.getNewPassword().isBlank()) {
                throw new BadRequestException("New password cannot be blank");
            }
            
            if (request.getNewPassword().length() < 8) {
                throw new BadRequestException("Password must be at least 8 characters long");
            }
            
            // Update password
            account.setPassword(passwordEncoder.encode(request.getNewPassword()));
            accountRepository.save(account);
            
            log.info("Password changed for user: {}", auth.getName());
            return BaseResponse.accepted(null, "Password changed successfully");
        } catch (BadRequestException e) {
            log.error("Bad request during password change", e);
            return BaseResponse.badRequest(e.getMessage(), e, null);
        } catch (AuthenticationException e) {
            log.error("Authentication error during password change", e);
            return BaseResponse.unauthorized(e.getMessage());
        } catch (Exception e) {
            log.error("Failed to change password", e);
            return BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to change password", e, null);
        }
    }
    
    /**
     * Get all accounts in the system
     * @return List of all accounts with basic information
     */
    public BaseResponse<List<AccountListResponse>> getAllAccounts() {
        try {
            List<User> users = userRepository.findAll();
            
            List<AccountListResponse> accountResponses = users.stream()
                .map(user -> {
                    Account account = user.getAccount();
                    AccountListResponse response = AccountListResponse.builder()
                        .id(account.getId())
                        .username(account.getUsername())
                        .fullName(user.getFullName())
                        .role(user.getRole())
                        .email(user.getEmail())
                        .phoneNumber(user.getPhoneNumber())
                        .status(account.getStatus())
                        .createdAt(account.getCreatedAt())
                        .userId(user.getId())
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
            
            return BaseResponse.ok(accountResponses, "List of all accounts retrieved successfully");
        } catch (Exception e) {
            log.error("Failed to retrieve account list", e);
            return BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to retrieve account list", e, null);
        }
    }
    
    /**
     * Reset a user's password
     * @param request containing username and new password
     * @return Success or error message
     */
    @Transactional
    public BaseResponse<String> resetPassword(ResetPasswordRequest request) {
        try {
            // Find the account
            Account account = accountRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with username: " + request.getUsername()));
            
            // Validate new password
            if (request.getNewPassword() == null || request.getNewPassword().isBlank()) {
                throw new BadRequestException("New password cannot be blank");
            }
            
            if (request.getNewPassword().length() < 8) {
                throw new BadRequestException("Password must be at least 8 characters long");
            }
            
            // Update password
            account.setPassword(passwordEncoder.encode(request.getNewPassword()));
            accountRepository.save(account);
            
            // TODO: Send notification to user about password reset (to be implemented later)
            
            log.info("Password reset for user: {}", request.getUsername());
            return BaseResponse.accepted(null, "Password reset successfully");
        } catch (ResourceNotFoundException e) {
            log.error("Account not found during password reset", e);
            return BaseResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage(), e, null);
        } catch (BadRequestException e) {
            log.error("Bad request during password reset", e);
            return BaseResponse.badRequest(e.getMessage(), e, null);
        } catch (Exception e) {
            log.error("Failed to reset password", e);
            return BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to reset password", e, null);
        }
    }
    
    /**
     * Create a single account
     * @param request containing account details
     * @return Success or error message
     */
    @Transactional
    public BaseResponse<String> createSingleAccount(CreateAccountRequest request) {
        try {
            // Check if username already exists
            if (accountRepository.findByUsername(request.getUsername()).isPresent()) {
                throw new BadRequestException("Username already exists: " + request.getUsername());
            }
            if(userRepository.existsByEmail(request.getEmail())){
                throw new BadRequestException("Email already exists: " + request.getEmail());
            }
            
            // Create account
            Account account = Account.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .status(AccountStatus.ACTIVE)
                .build();
            account = accountRepository.save(account);
            
            // Create user
            User user = User.builder()
                .account(account)
                .role(request.getRole())
                .fullName(request.getFullName())
                .dob(request.getDob())
                .gender(request.getGender())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .avatarUrl(request.getAvatarUrl())
                .build();
            user = userRepository.save(user);
            
            // Create role-specific records based on the role
            switch (request.getRole()) {
                case ADMIN:
                    Admin admin = Admin.builder()
                        .user(user)
                        .build();
                    adminRepository.save(admin);
                    break;
                case TEACHER:
                    Teacher teacher = Teacher.builder()
                        .user(user)
                        .build();
                    teacher = teacherRepository.save(teacher);
                    
                    // Create empty teacher information record
                    TeacherInformation teacherInfo = TeacherInformation.builder()
                        .teacher(teacher)
                        .diplomaLevel(DiplomaLevel.BACHELOR) // Default diploma level
                        .build();
                    teacherInformationRepository.save(teacherInfo);
                    break;
                case STUDENT:
                    Student student = Student.builder()
                        .user(user)
                        .build();
                    student = studentRepository.save(student);
                    
                    // Create empty student information record
                    StudentInformation studentInfo = StudentInformation.builder()
                        .student(student)
                        .build();
                    studentInformationRepository.save(studentInfo);
                    break;
            }
            
            log.info("Created new account with username: {}, role: {}", request.getUsername(), request.getRole());
            return BaseResponse.created(null, "Account created successfully");
        } catch (BadRequestException e) {
            log.error("Bad request during account creation", e);
            return BaseResponse.badRequest(e.getMessage(), e, null);
        } catch (Exception e) {
            log.error("Failed to create account", e);
            return BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to create account", e, null);
        }
    }
}
