package service.search;

import dto.response.admin.SubjectListResponse;
import dto.response.search.UserDetailResponse;
import dto.response.search.UserSearchResponse;
import exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import model.Subject;
import model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.AccountRepository;
import repository.SubjectRepository;
import repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl {

    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;

    /**
     * Search users by username or email
     * @param query The search query (username or email)
     * @return List of matching users
     */
    @Transactional(readOnly = true)
    public List<UserSearchResponse> searchUser(String query) {
        if (query == null || query.trim().isEmpty()) {
            return List.of();
        }

        // Sanitize the input
        String sanitizedQuery = sanitizeInput(query);

        // Search by username or email
        return userRepository.findAll().stream()
                .filter(user ->
                        (user.getAccount().getUsername() != null &&
                                user.getAccount().getUsername().toLowerCase().contains(sanitizedQuery.toLowerCase())) ||
                                (user.getEmail() != null &&
                                        user.getEmail().toLowerCase().contains(sanitizedQuery.toLowerCase()))
                )
                .map(this::mapToUserSearchResponse)
                .collect(Collectors.toList());
    }

    /**
     * View user information by user ID
     * @param userId The user ID
     * @return User detail information
     */
    public UserDetailResponse viewInformation(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        return mapToUserDetailResponse(user);
    }

    /**
     * Search subjects by name or code
     * @param query The search query (name or code)
     * @return List of matching subjects
     */
    @Transactional(readOnly = true)
    public List<SubjectListResponse> searchSubject(String query) {
        if (query == null || query.trim().isEmpty()) {
            return List.of();
        }

        // Sanitize the input
        String sanitizedQuery = sanitizeInput(query);

        // Search by name or code
        return subjectRepository.findAll().stream()
                .filter(subject ->
                        (subject.getName() != null &&
                                subject.getName().toLowerCase().contains(sanitizedQuery.toLowerCase())) ||
                                (subject.getCode() != null &&
                                        subject.getCode().toLowerCase().contains(sanitizedQuery.toLowerCase()))
                )
                .map(this::mapToSubjectListResponse)
                .collect(Collectors.toList());
    }

    /**
     * Sanitize input to prevent injection attacks
     * @param input The input to sanitize
     * @return Sanitized input
     */
    private String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }

        // Remove any special characters that could be used for SQL injection
        return input.replaceAll("[;'\"\\\\/]", "");
    }

    /**
     * Map User entity to UserSearchResponse DTO
     * @param user The user entity
     * @return UserSearchResponse DTO
     */
    private UserSearchResponse mapToUserSearchResponse(User user) {
        return UserSearchResponse.builder()
                .id(user.getId())
                .username(user.getAccount().getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    /**
     * Map User entity to UserDetailResponse DTO
     * @param user The user entity
     * @return UserDetailResponse DTO
     */
    private UserDetailResponse mapToUserDetailResponse(User user) {
        return UserDetailResponse.builder()
                .id(user.getId())
                .username(user.getAccount().getUsername())
                .accountStatus(user.getAccount().getStatus())
                .role(user.getRole())
                .fullName(user.getFullName())
                .dob(user.getDob())
                .gender(user.getGender())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }

    /**
     * Map Subject entity to SubjectListResponse DTO
     * @param subject The subject entity
     * @return SubjectListResponse DTO
     */
    private SubjectListResponse mapToSubjectListResponse(Subject subject) {
        return SubjectListResponse.builder()
                .id(subject.getId())
                .name(subject.getName())
                .code(subject.getCode())
                .description(subject.getDescription())
                .totalClasses(subject.getClasses() != null ? subject.getClasses().size() : 0)
                .createdAt(subject.getCreatedAt())
                .updatedAt(subject.getUpdatedAt())
                .build();
    }
}