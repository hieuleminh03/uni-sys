package service.admin;

import dto.request.admin.CreateSubjectRequest;
import dto.request.admin.UpdateSubjectRequest;
import dto.response.admin.SubjectDetailResponse;
import dto.response.admin.SubjectListResponse;
import exception.BadRequestException;
import exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import model.Subject;
import model.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.SubjectRepository;
import util.Paging;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminSubjectServiceImpl {

    private final SubjectRepository subjectRepository;

    /**
     * Get all subjects with pagination
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getAllSubjects(Pageable pageable) {
        Page<Subject> subjectsPage = subjectRepository.findAllSubjects(pageable);
        
        List<SubjectListResponse> subjectResponses = subjectsPage.getContent().stream()
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
        
        return response;
    }

    /**
     * Get subject by ID with class information
     */
    @Transactional(readOnly = true)
    public SubjectDetailResponse getSubjectById(Long id) {
        Subject subject = subjectRepository.findByIdWithClasses(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with ID: " + id));
        
        return mapToSubjectDetailResponse(subject);
    }

    /**
     * Create a new subject
     */
    @Transactional
    public SubjectListResponse createSubject(CreateSubjectRequest request) {
        // Check if subject with the same code already exists
        if (subjectRepository.findByCode(request.getCode()).isPresent()) {
            throw new BadRequestException("Subject with code " + request.getCode() + " already exists");
        }
        
        // Check if subject with the same name already exists
        if (subjectRepository.findByName(request.getName()).isPresent()) {
            throw new BadRequestException("Subject with name " + request.getName() + " already exists");
        }
        
        Subject subject = Subject.builder()
                .name(request.getName())
                .code(request.getCode())
                .description(request.getDescription())
                .build();
        
        Subject savedSubject = subjectRepository.save(subject);
        
        return mapToSubjectListResponse(savedSubject);
    }

    /**
     * Update an existing subject
     */
    @Transactional
    public SubjectListResponse updateSubject(UpdateSubjectRequest request) {
        Subject subject = subjectRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with ID: " + request.getId()));
        
        // Check if code is being changed and if the new code already exists for another subject
        if (!subject.getCode().equals(request.getCode())) {
            subjectRepository.findByCode(request.getCode())
                    .ifPresent(s -> {
                        if (!s.getId().equals(request.getId())) {
                            throw new BadRequestException("Subject with code " + request.getCode() + " already exists");
                        }
                    });
        }
        
        // Check if name is being changed and if the new name already exists for another subject
        if (!subject.getName().equals(request.getName())) {
            subjectRepository.findByName(request.getName())
                    .ifPresent(s -> {
                        if (!s.getId().equals(request.getId())) {
                            throw new BadRequestException("Subject with name " + request.getName() + " already exists");
                        }
                    });
        }
        
        subject.setName(request.getName());
        subject.setCode(request.getCode());
        subject.setDescription(request.getDescription());
        
        Subject updatedSubject = subjectRepository.save(subject);
        
        return mapToSubjectListResponse(updatedSubject);
    }

    /**
     * Helper method to map Subject to SubjectListResponse
     */
    private SubjectListResponse mapToSubjectListResponse(Subject subject) {
        int totalClasses = subject.getClasses() != null ? subject.getClasses().size() : 0;
        
        return SubjectListResponse.builder()
                .id(subject.getId())
                .name(subject.getName())
                .code(subject.getCode())
                .description(subject.getDescription())
                .totalClasses(totalClasses)
                .createdAt(subject.getCreatedAt())
                .updatedAt(subject.getUpdatedAt())
                .build();
    }

    /**
     * Helper method to map Subject to SubjectDetailResponse
     */
    private SubjectDetailResponse mapToSubjectDetailResponse(Subject subject) {
        List<SubjectDetailResponse.ClassInfo> classInfos = null;
        
        if (subject.getClasses() != null && !subject.getClasses().isEmpty()) {
            classInfos = subject.getClasses().stream()
                    .map(clazz -> {

                        
                        return SubjectDetailResponse.ClassInfo.builder()
                                .id(clazz.getId())
                                .name(clazz.getClassName())
                                .teacherName(clazz.getTeacher().getUser().getFullName())
                                .teacherEmail(clazz.getTeacher().getUser().getEmail())
                                .teacherAvatarUrl(clazz.getTeacher().getUser().getAvatarUrl())
                                .totalStudents(clazz.getStudents() != null ? clazz.getStudents().size() : 0)
                                .startDate(clazz.getStartDate())
                                .endDate(clazz.getEndDate())
                                .build();
                    })
                    .collect(Collectors.toList());
        }
        
        return SubjectDetailResponse.builder()
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
}