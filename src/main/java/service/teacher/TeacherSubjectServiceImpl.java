package service.teacher;

import dto.response.BaseResponse;
import dto.response.teacher.TeacherSubjectDetailResponse;
import dto.response.teacher.TeacherSubjectListResponse;
import exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.Class;
import model.Subject;
import model.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.ClassRepositoryCustom;
import repository.SubjectRepository;
import repository.TeacherRepository;
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
public class TeacherSubjectServiceImpl {

    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final ClassRepositoryCustom classRepository;

    /**
     * Get all subjects with pagination
     */
    @Transactional(readOnly = true)
    public BaseResponse<Map<String, Object>> getAllSubjects(Pageable pageable) {
        try {
            // Get all subjects with pagination
            Page<Subject> subjectsPage = subjectRepository.findAllSubjects(pageable);
            
            List<TeacherSubjectListResponse> subjectResponses = subjectsPage.getContent().stream()
                    .map(this::mapToSubjectListResponse)
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
     * Get current subjects that the teacher is teaching
     */
    @Transactional(readOnly = true)
    public BaseResponse<List<TeacherSubjectListResponse>> getCurrentSubjects() {
        try {
            // Get current authenticated teacher
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            Teacher teacher = teacherRepository.findByUserAccountUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
            
            // Get all classes taught by this teacher
            List<Class> teacherClasses = classRepository.findByTeacher(teacher);
            
            if (teacherClasses.isEmpty()) {
                return BaseResponse.ok(
                    List.of(),
                    "No subjects found for this teacher"
                );
            }
            
            // Group classes by subject
            Map<Subject, List<Class>> subjectClassesMap = teacherClasses.stream()
                .collect(Collectors.groupingBy(Class::getSubject));
            
            List<TeacherSubjectListResponse> responses = subjectClassesMap.entrySet().stream()
                .map(entry -> mapToTeacherSubjectListResponse(entry.getKey(), entry.getValue().size()))
                .collect(Collectors.toList());
            
            return BaseResponse.ok(responses, "Current subjects retrieved successfully");
        } catch (ResourceNotFoundException e) {
            log.error("Teacher not found", e);
            return BaseResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage(), e, null);
        } catch (Exception e) {
            log.error("Failed to retrieve current subjects", e);
            return BaseResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                "Failed to retrieve current subjects", 
                e, 
                null
            );
        }
    }

    /**
     * Get subject by ID with class information
     */
    @Transactional(readOnly = true)
    public BaseResponse<TeacherSubjectDetailResponse> getSubjectById(Long id) {
        try {
            // Get current authenticated teacher
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            Teacher teacher = teacherRepository.findByUserAccountUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
            
            Subject subject = subjectRepository.findByIdWithClasses(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with ID: " + id));
            
            // Get all classes taught by this teacher for this subject
            Set<Long> teachingClassIds = classRepository.findByTeacherAndSubject(teacher, subject).stream()
                .map(Class::getId)
                .collect(Collectors.toSet());
            
            TeacherSubjectDetailResponse response = mapToSubjectDetailResponse(subject, teachingClassIds);
            
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
     * Helper method to map Subject to TeacherSubjectListResponse
     */
    private TeacherSubjectListResponse mapToSubjectListResponse(Subject subject) {
        int totalClasses = subject.getClasses() != null ? subject.getClasses().size() : 0;
        
        return TeacherSubjectListResponse.builder()
            .id(subject.getId())
            .name(subject.getName())
            .code(subject.getCode())
            .description(subject.getDescription())
            .totalClasses(totalClasses)
            .teachingClasses(0) // Default to 0 for general listing
            .createdAt(subject.getCreatedAt())
            .updatedAt(subject.getUpdatedAt())
            .build();
    }
    
    /**
     * Helper method to map Subject to TeacherSubjectListResponse with teaching classes count
     */
    private TeacherSubjectListResponse mapToTeacherSubjectListResponse(Subject subject, int teachingClasses) {
        int totalClasses = subject.getClasses() != null ? subject.getClasses().size() : 0;
        
        return TeacherSubjectListResponse.builder()
            .id(subject.getId())
            .name(subject.getName())
            .code(subject.getCode())
            .description(subject.getDescription())
            .totalClasses(totalClasses)
            .teachingClasses(teachingClasses)
            .createdAt(subject.getCreatedAt())
            .updatedAt(subject.getUpdatedAt())
            .build();
    }

    /**
     * Helper method to map Subject to TeacherSubjectDetailResponse
     */
    private TeacherSubjectDetailResponse mapToSubjectDetailResponse(Subject subject, Set<Long> teachingClassIds) {
        List<TeacherSubjectDetailResponse.ClassInfo> classInfos = null;
        
        if (subject.getClasses() != null && !subject.getClasses().isEmpty()) {
            classInfos = subject.getClasses().stream()
                .map(clazz -> {
                    boolean teaching = teachingClassIds.contains(clazz.getId());
                    return mapToClassInfo(clazz, teaching);
                })
                .collect(Collectors.toList());
        }
        
        return TeacherSubjectDetailResponse.builder()
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
    private TeacherSubjectDetailResponse.ClassInfo mapToClassInfo(Class clazz, boolean teaching) {
        return TeacherSubjectDetailResponse.ClassInfo.builder()
            .id(clazz.getId())
            .name(clazz.getClassName())
            .teaching(teaching)
            .totalStudents(clazz.getStudents() != null ? clazz.getStudents().size() : 0)
            .startDate(LocalDateTime.ofInstant(clazz.getStartDate().toInstant(),
                    ZoneId.systemDefault()))
            .endDate(LocalDateTime.ofInstant(clazz.getEndDate().toInstant(),
                    ZoneId.systemDefault()))
            .build();
    }
}