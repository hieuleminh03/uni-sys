package service.student;

import dto.response.BaseResponse;
import dto.response.student.StudentSubjectDetailResponse;
import dto.response.student.StudentSubjectListResponse;
import exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.Class;
import model.ClassStudent;
import model.Student;
import model.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.ClassStudentRepositoryCustom;
import repository.StudentRepository;
import repository.SubjectRepository;
import util.Paging;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentSubjectServiceImpl {

    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;
    private final ClassStudentRepositoryCustom classStudentRepository;

    /**
     * Get all subjects with pagination
     */
    @Transactional(readOnly = true)
    public BaseResponse<Map<String, Object>> getAllSubjects(Pageable pageable) {
        try {
            // Get current authenticated student
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            Student student = studentRepository.findByUserAccountUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

            // Get all subjects with pagination
            Page<Subject> subjectsPage = subjectRepository.findAllSubjects(pageable);

            // Get all classes the student is enrolled in
            List<ClassStudent> enrolledClasses = classStudentRepository.findByStudent(student);

            // TODO: use the class repository to get all classes
            Set<Long> enrolledSubjectIds = enrolledClasses.stream()
                    .map(cs -> cs.getClassEntity().getSubject().getId())
                    .collect(Collectors.toSet());

            List<StudentSubjectListResponse> subjectResponses = subjectsPage.getContent().stream()
                    .map(subject -> mapToSubjectListResponse(subject, enrolledSubjectIds.contains(subject.getId())))
                    .collect(Collectors.toList());

            Paging paging = Paging.builder()
                    .page(subjectsPage.getNumber())
                    .size(subjectsPage.getSize())
                    .totalElements(subjectsPage.getTotalElements())
                    .totalPages(subjectsPage.getTotalPages())
                    .build();

            Map<String, Object> response = new HashMap<>();
            response.put("subjects", subjectResponses);
            response.put("paging", paging);

            return BaseResponse.ok(response, "All subjects retrieved successfully");
        } catch (ResourceNotFoundException e) {
            log.error("Student not found", e);
            return BaseResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage(), e, null);
        } catch (Exception e) {
            log.error("Failed to retrieve subjects", e);
            return BaseResponse.error(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Failed to retrieve subjects",
                    e,
                    null
            );
        }
    }

    /**
     * Get subject by ID with class information
     */
    @Transactional(readOnly = true)
    public BaseResponse<StudentSubjectDetailResponse> getSubjectById(Long id) {
        try {
            // Get current authenticated student
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            Student student = studentRepository.findByUserAccountUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

            Subject subject = subjectRepository.findByIdWithClasses(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Subject not found with ID: " + id));

            // Get all classes the student is enrolled in for this subject
            // Get all classes the student is enrolled in for this subject
            List<ClassStudent> enrolledClasses = classStudentRepository.findByStudentAndSubject(student, subject);

            Set<Long> enrolledClassIds = enrolledClasses.stream()
                    .map(cs -> cs.getClassEntity().getId())
                    .collect(Collectors.toSet());

            StudentSubjectDetailResponse response = mapToSubjectDetailResponse(subject, enrolledClassIds);

            return BaseResponse.ok(response, "Subject information retrieved successfully");
        } catch (ResourceNotFoundException e) {
            log.error("Error retrieving subject details", e);
            return BaseResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage(), e, null);
        } catch (Exception e) {
            log.error("Failed to retrieve subject details", e);
            return BaseResponse.error(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Failed to retrieve subject details",
                    e,
                    null
            );
        }
    }

    /**
     * Helper method to map Subject to StudentSubjectListResponse
     */
    private StudentSubjectListResponse mapToSubjectListResponse(Subject subject, boolean isEnrolled) {
        int totalClasses = subject.getClasses() != null ? subject.getClasses().size() : 0;
        int enrolledClasses = isEnrolled ? 1 : 0; // Simplified, in reality would count actual enrolled classes

        return StudentSubjectListResponse.builder()
                .id(subject.getId())
                .name(subject.getName())
                .code(subject.getCode())
                .description(subject.getDescription())
                .totalClasses(totalClasses)
                .enrolledClasses(enrolledClasses)
                .createdAt(subject.getCreatedAt())
                .updatedAt(subject.getUpdatedAt())
                .build();
    }

    /**
     * Helper method to map Subject to StudentSubjectDetailResponse
     */
    private StudentSubjectDetailResponse mapToSubjectDetailResponse(Subject subject, Set<Long> enrolledClassIds) {
        List<StudentSubjectDetailResponse.ClassInfo> classInfos = null;

        if (subject.getClasses() != null && !subject.getClasses().isEmpty()) {
            classInfos = subject.getClasses().stream()
                    .map(clazz -> {
                        boolean enrolled = enrolledClassIds.contains(clazz.getId());
                        return mapToClassInfo(clazz, enrolled);
                    })
                    .collect(Collectors.toList());
        }

        return StudentSubjectDetailResponse.builder()
                .id(subject.getId())
                .name(subject.getName())
                .code(subject.getCode())
                .description(subject.getDescription())
                .totalClasses(subject.getClasses() != null ? subject.getClasses().size() : 0)
                .classes(classInfos)
                .createdAt(subject.getCreatedAt())
                .updatedAt(subject.getUpdatedAt())
                .build();
    }

    /**
     * Helper method to map Class to ClassInfo
     */
    private StudentSubjectDetailResponse.ClassInfo mapToClassInfo(Class clazz, boolean enrolled) {
        return StudentSubjectDetailResponse.ClassInfo.builder()
                .id(clazz.getId())
                .name(clazz.getClassName())
                .teacherName(clazz.getTeacher().getUser().getFullName())
                .teacherEmail(clazz.getTeacher().getUser().getEmail())
                .teacherAvatarUrl(clazz.getTeacher().getUser().getAvatarUrl())
                .totalStudents(clazz.getStudents() != null ? clazz.getStudents().size() : 0)
                .enrolled(enrolled)
                .startDate(clazz.getStartDate())
                .endDate(clazz.getEndDate())
                .build();
    }
}