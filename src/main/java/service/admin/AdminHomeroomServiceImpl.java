package service.admin;

import dto.request.admin.AddStudentToHomeroomRequest;
import dto.request.admin.BatchAddStudentsRequest;
import dto.request.admin.BatchRemoveStudentsRequest;
import dto.request.admin.CreateHomeroomRequest;
import dto.request.admin.RemoveStudentFromHomeroomRequest;
import dto.request.admin.UpdateHomeroomRequest;
import dto.request.admin.UpdateHomeroomTeacherRequest;
import dto.response.BaseResponse;
import dto.response.admin.HomeroomDetailResponse;
import dto.response.admin.HomeroomListResponse;
import dto.response.admin.HomeroomStudentResponse;
import exception.BadRequestException;
import exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.Homeroom;
import model.HomeroomStudent;
import model.Student;
import model.Teacher;
import model.enums.HomeroomStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.HomeroomRepository;
import repository.HomeroomStudentRepository;
import repository.StudentRepository;
import repository.TeacherRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminHomeroomServiceImpl {

    private final HomeroomRepository homeroomRepository;
    private final HomeroomStudentRepository homeroomStudentRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    /**
     * Get all homerooms with pagination
     */
    @Transactional(readOnly = true)
    public BaseResponse<List<HomeroomListResponse>> getAllHomerooms(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Homeroom> homeroomPage = homeroomRepository.findAll(pageable);
            
            if (homeroomPage.isEmpty()) {
                return BaseResponse.ok(
                    List.of(),
                    "No homerooms found",
                    new util.Paging(page, size, 0, 0)
                );
            }
            
            // Extract IDs for eager fetching
            List<Long> homeroomIds = homeroomPage.getContent().stream()
                .map(Homeroom::getId)
                .collect(Collectors.toList());
            
            // Fetch full entities with eager loading
            List<Homeroom> homerooms = homeroomRepository.findAllWithTeacherByIds(homeroomIds);
            
            // Create a map of homeroom IDs to student counts
            Map<Long, Long> studentCountsMap = homerooms.stream()
                .collect(Collectors.toMap(
                    Homeroom::getId,
                    homeroom -> homeroomStudentRepository.countByHomeroom(homeroom)
                ));
            
            List<HomeroomListResponse> responseList = homerooms.stream()
                .map(homeroom -> mapToListResponse(homeroom, studentCountsMap.getOrDefault(homeroom.getId(), 0L)))
                .collect(Collectors.toList());
            
            return BaseResponse.ok(
                responseList,
                "Homerooms retrieved successfully",
                new util.Paging(
                    page,
                    size,
                    homeroomPage.getTotalElements(),
                    homeroomPage.getTotalPages()
                )
            );
        } catch (Exception e) {
            log.error("Failed to retrieve homerooms", e);
            return BaseResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                "Failed to retrieve homerooms", 
                e, 
                null
            );
        }
    }

    /**
     * Get homeroom by ID
     */
    @Transactional(readOnly = true)
    public BaseResponse<HomeroomDetailResponse> getHomeroomById(Long id) {
        try {
            Homeroom homeroom = homeroomRepository.findByIdWithTeacherAndStudents(id)
                .orElseThrow(() -> new ResourceNotFoundException("Homeroom not found with id: " + id));
            
            HomeroomDetailResponse response = mapToDetailResponse(homeroom);
            return BaseResponse.ok(response, "Homeroom retrieved successfully");
        } catch (ResourceNotFoundException e) {
            log.error("Homeroom not found", e);
            return BaseResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage(), e, null);
        } catch (Exception e) {
            log.error("Failed to retrieve homeroom", e);
            return BaseResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                "Failed to retrieve homeroom", 
                e, 
                null
            );
        }
    }

    /**
     * Create a new homeroom
     */
    @Transactional
    public BaseResponse<String> createHomeroom(CreateHomeroomRequest request) {
        try {
            // Check if homeroom name already exists
            if (homeroomRepository.findByName(request.getName()).isPresent()) {
                throw new BadRequestException("Homeroom with name " + request.getName() + " already exists");
            }
            
            // Get the teacher
            Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + request.getTeacherId()));
            
            // Create new homeroom
            Homeroom homeroom = Homeroom.builder()
                .name(request.getName())
                .teacher(teacher)
                .students(new ArrayList<>())
                .build();
            
            homeroomRepository.save(homeroom);
            
            log.info("Created new homeroom with name: {}, teacher: {}", request.getName(), teacher.getId());
            
            return BaseResponse.created(null, "Homeroom created successfully");
        } catch (ResourceNotFoundException | BadRequestException e) {
            log.error("Error creating homeroom", e);
            return BaseResponse.error(HttpStatus.BAD_REQUEST.value(), e.getMessage(), e, null);
        } catch (Exception e) {
            log.error("Failed to create homeroom", e);
            return BaseResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                "Failed to create homeroom", 
                e, 
                null
            );
        }
    }

    /**
     * Update an existing homeroom's name
     */
    @Transactional
    public BaseResponse<String> updateHomeroom(UpdateHomeroomRequest request) {
        try {
            // Find the homeroom by ID
            Homeroom homeroom = homeroomRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Homeroom not found with id: " + request.getId()));
            
            // Check if new name already exists (but not for this homeroom)
            if (!homeroom.getName().equals(request.getName()) && 
                homeroomRepository.findByName(request.getName()).isPresent()) {
                throw new BadRequestException("Homeroom with name " + request.getName() + " already exists");
            }
            
            // Update the homeroom name
            homeroom.setName(request.getName());
            
            homeroomRepository.save(homeroom);
            
            log.info("Updated homeroom with id: {}, new name: {}", request.getId(), request.getName());
            
            return BaseResponse.accepted(null, "Homeroom updated successfully");
        } catch (ResourceNotFoundException | BadRequestException e) {
            log.error("Error updating homeroom", e);
            return BaseResponse.error(HttpStatus.BAD_REQUEST.value(), e.getMessage(), e, null);
        } catch (Exception e) {
            log.error("Failed to update homeroom", e);
            return BaseResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                "Failed to update homeroom", 
                e, 
                null
            );
        }
    }

    /**
     * Update homeroom teacher
     */
    @Transactional
    public BaseResponse<String> updateHomeroomTeacher(UpdateHomeroomTeacherRequest request) {
        try {
            // Find the homeroom by ID
            Homeroom homeroom = homeroomRepository.findById(request.getHomeroomId())
                .orElseThrow(() -> new ResourceNotFoundException("Homeroom not found with id: " + request.getHomeroomId()));
            
            // If teacherId is null, it means the teacher should be removed
            // But this is not allowed as a homeroom must have a teacher
            if (request.getTeacherId() == null) {
                throw new BadRequestException("Homeroom must have a teacher assigned");
            }
            
            // Get the new teacher
            Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + request.getTeacherId()));
            
            // Update the homeroom teacher
            homeroom.setTeacher(teacher);
            
            homeroomRepository.save(homeroom);
            
            log.info("Updated homeroom teacher: homeroom id: {}, new teacher id: {}", 
                request.getHomeroomId(), request.getTeacherId());
            
            return BaseResponse.accepted(null, "Homeroom teacher updated successfully");
        } catch (ResourceNotFoundException | BadRequestException e) {
            log.error("Error updating homeroom teacher", e);
            return BaseResponse.error(HttpStatus.BAD_REQUEST.value(), e.getMessage(), e, null);
        } catch (Exception e) {
            log.error("Failed to update homeroom teacher", e);
            return BaseResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                "Failed to update homeroom teacher", 
                e, 
                null
            );
        }
    }

    /**
     * Add a student to homeroom
     */
    @Transactional
    public BaseResponse<String> addStudentToHomeroom(AddStudentToHomeroomRequest request) {
        try {
            // Find the homeroom by ID
            Homeroom homeroom = homeroomRepository.findById(request.getHomeroomId())
                .orElseThrow(() -> new ResourceNotFoundException("Homeroom not found with id: " + request.getHomeroomId()));
            
            // Get the student
            Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + request.getStudentId()));
            
            // Check if student is already in this homeroom
            if (homeroomStudentRepository.findByHomeroomAndStudent(homeroom, student).isPresent()) {
                throw new BadRequestException("Student already exists in this homeroom");
            }
            
            // Check if student is already in another homeroom with ANTICIPATED status
            if (request.getStatus() == HomeroomStatus.ANTICIPATED && 
                homeroomStudentRepository.existsByStudentAndStatus(student, HomeroomStatus.ANTICIPATED)) {
                throw new BadRequestException("Student is already active in another homeroom");
            }
            
            // Create new homeroom-student relationship
            HomeroomStudent homeroomStudent = HomeroomStudent.builder()
                .homeroom(homeroom)
                .student(student)
                .status(request.getStatus())
                .build();
            
            homeroomStudentRepository.save(homeroomStudent);
            
            log.info("Added student id: {} to homeroom id: {} with status: {}", 
                request.getStudentId(), request.getHomeroomId(), request.getStatus());
            
            return BaseResponse.created(null, "Student added to homeroom successfully");
        } catch (ResourceNotFoundException | BadRequestException e) {
            log.error("Error adding student to homeroom", e);
            return BaseResponse.error(HttpStatus.BAD_REQUEST.value(), e.getMessage(), e, null);
        } catch (Exception e) {
            log.error("Failed to add student to homeroom", e);
            return BaseResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                "Failed to add student to homeroom", 
                e, 
                null
            );
        }
    }

    /**
     * Batch add students to homeroom
     */
    @Transactional
    public BaseResponse<Map<String, Object>> addStudentsToHomeroom(BatchAddStudentsRequest request) {
        try {
            // Find the homeroom by ID
            Homeroom homeroom = homeroomRepository.findById(request.getHomeroomId())
                .orElseThrow(() -> new ResourceNotFoundException("Homeroom not found with id: " + request.getHomeroomId()));
            
            // Get all students
            List<Student> students = studentRepository.findAllById(request.getStudentIds());
            if (students.size() != request.getStudentIds().size()) {
                throw new ResourceNotFoundException("Some students were not found");
            }
            
            List<Map<String, String>> failures = new ArrayList<>();
            List<Long> addedStudentIds = new ArrayList<>();
            
            for (Student student : students) {
                try {
                    // Check if student is already in this homeroom
                    if (homeroomStudentRepository.findByHomeroomAndStudent(homeroom, student).isPresent()) {
                        Map<String, String> failure = new HashMap<>();
                        failure.put("studentId", student.getId().toString());
                        failure.put("reason", "Student already exists in this homeroom");
                        failures.add(failure);
                        continue;
                    }
                    
                    // Check if student is already in another homeroom with ANTICIPATED status
                    if (request.getStatus() == HomeroomStatus.ANTICIPATED &&
                        homeroomStudentRepository.existsByStudentAndStatus(student, HomeroomStatus.ANTICIPATED)) {
                        Map<String, String> failure = new HashMap<>();
                        failure.put("studentId", student.getId().toString());
                        failure.put("reason", "Student is already active in another homeroom");
                        failures.add(failure);
                        continue;
                    }
                    
                    // Create new homeroom-student relationship
                    HomeroomStudent homeroomStudent = HomeroomStudent.builder()
                        .homeroom(homeroom)
                        .student(student)
                        .status(request.getStatus())
                        .build();
                    
                    homeroomStudentRepository.save(homeroomStudent);
                    addedStudentIds.add(student.getId());
                } catch (Exception e) {
                    Map<String, String> failure = new HashMap<>();
                    failure.put("studentId", student.getId().toString());
                    failure.put("reason", "Error: " + e.getMessage());
                    failures.add(failure);
                    log.error("Error adding student to homeroom batch", e);
                }
            }
            
            // Build the response with detailed information
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("addedStudents", addedStudentIds);
            responseData.put("failedStudents", failures);
            responseData.put("totalRequested", request.getStudentIds().size());
            responseData.put("totalAdded", addedStudentIds.size());
            responseData.put("totalFailed", failures.size());
            
            StringBuilder message = new StringBuilder("Batch student addition completed. ");
            message.append(addedStudentIds.size()).append(" students added successfully");
            
            if (!failures.isEmpty()) {
                message.append(", ").append(failures.size()).append(" students failed to add");
            }
            
            log.info("Batch added {} students to homeroom id: {}", addedStudentIds.size(), request.getHomeroomId());
            
            return BaseResponse.created(responseData, message.toString());
        } catch (ResourceNotFoundException e) {
            log.error("Error in batch adding students to homeroom", e);
            return BaseResponse.error(HttpStatus.BAD_REQUEST.value(), e.getMessage(), e, null);
        } catch (Exception e) {
            log.error("Failed in batch adding students to homeroom", e);
            return BaseResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Failed in batch adding students to homeroom",
                e,
                null
            );
        }
    }

    /**
     * Remove a student from homeroom
     */
    @Transactional
    public BaseResponse<String> removeStudentFromHomeroom(RemoveStudentFromHomeroomRequest request) {
        try {
            // Find the homeroom by ID
            Homeroom homeroom = homeroomRepository.findById(request.getHomeroomId())
                .orElseThrow(() -> new ResourceNotFoundException("Homeroom not found with id: " + request.getHomeroomId()));
            
            // Get the student
            Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + request.getStudentId()));
            
            // Find the homeroom-student relationship
            HomeroomStudent homeroomStudent = homeroomStudentRepository.findByHomeroomAndStudent(homeroom, student)
                .orElseThrow(() -> new ResourceNotFoundException("Student is not in this homeroom"));
            
            // Remove the student from homeroom
            homeroomStudentRepository.delete(homeroomStudent);
            
            log.info("Removed student id: {} from homeroom id: {}", 
                request.getStudentId(), request.getHomeroomId());
            
            return BaseResponse.accepted(null, "Student removed from homeroom successfully");
        } catch (ResourceNotFoundException e) {
            log.error("Error removing student from homeroom", e);
            return BaseResponse.error(HttpStatus.BAD_REQUEST.value(), e.getMessage(), e, null);
        } catch (Exception e) {
            log.error("Failed to remove student from homeroom", e);
            return BaseResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                "Failed to remove student from homeroom", 
                e, 
                null
            );
        }
    }

    /**
     * Batch remove students from homeroom
     */
    @Transactional
    public BaseResponse<Map<String, Object>> removeStudentsFromHomeroom(BatchRemoveStudentsRequest request) {
        try {
            // Find the homeroom by ID
            Homeroom homeroom = homeroomRepository.findById(request.getHomeroomId())
                .orElseThrow(() -> new ResourceNotFoundException("Homeroom not found with id: " + request.getHomeroomId()));
            
            // Get all students
            List<Student> students = studentRepository.findAllById(request.getStudentIds());
            if (students.isEmpty()) {
                throw new ResourceNotFoundException("No students found");
            }
            
            List<Map<String, String>> failures = new ArrayList<>();
            List<Long> removedStudentIds = new ArrayList<>();
            
            for (Student student : students) {
                try {
                    final Student currentStudent = student; // For use in lambda
                    // Find the homeroom-student relationship
                    homeroomStudentRepository.findByHomeroomAndStudent(homeroom, student)
                        .ifPresentOrElse(homeroomStudent -> {
                            // Remove the student from homeroom
                            homeroomStudentRepository.delete(homeroomStudent);
                            removedStudentIds.add(currentStudent.getId());
                        }, () -> {
                            Map<String, String> failure = new HashMap<>();
                            failure.put("studentId", currentStudent.getId().toString());
                            failure.put("reason", "Student is not in this homeroom");
                            failures.add(failure);
                        });
                } catch (Exception e) {
                    Map<String, String> failure = new HashMap<>();
                    failure.put("studentId", student.getId().toString());
                    failure.put("reason", "Error: " + e.getMessage());
                    failures.add(failure);
                    log.error("Error removing student from homeroom batch", e);
                }
            }
            
            // Build the response with detailed information
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("removedStudents", removedStudentIds);
            responseData.put("failedStudents", failures);
            responseData.put("totalRequested", request.getStudentIds().size());
            responseData.put("totalRemoved", removedStudentIds.size());
            responseData.put("totalFailed", failures.size());
            
            StringBuilder message = new StringBuilder("Batch student removal completed. ");
            message.append(removedStudentIds.size()).append(" students removed successfully");
            
            if (!failures.isEmpty()) {
                message.append(", ").append(failures.size()).append(" students failed to remove");
            }
            
            log.info("Batch removed {} students from homeroom id: {}", removedStudentIds.size(), request.getHomeroomId());
            
            return BaseResponse.accepted(responseData, message.toString());
        } catch (ResourceNotFoundException e) {
            log.error("Error in batch removing students from homeroom", e);
            return BaseResponse.error(HttpStatus.BAD_REQUEST.value(), e.getMessage(), e, null);
        } catch (Exception e) {
            log.error("Failed in batch removing students from homeroom", e);
            return BaseResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Failed in batch removing students from homeroom",
                e,
                null
            );
        }
    }

    /**
     * Update homeroom student information
     */
    @Transactional
    public BaseResponse<String> updateHomeroomStudentStatus(Long homeroomId, Long studentId, HomeroomStatus status) {
        try {
            // Find the homeroom by ID
            Homeroom homeroom = homeroomRepository.findById(homeroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Homeroom not found with id: " + homeroomId));
            
            // Get the student
            Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
            
            // Find the homeroom-student relationship
            HomeroomStudent homeroomStudent = homeroomStudentRepository.findByHomeroomAndStudent(homeroom, student)
                .orElseThrow(() -> new ResourceNotFoundException("Student is not in this homeroom"));
            
            // Check if trying to set to ANTICIPATED when already in another homeroom with that status
            if (status == HomeroomStatus.ANTICIPATED && 
                homeroomStudent.getStatus() != HomeroomStatus.ANTICIPATED) {
                
                // Check if student is already in another homeroom with ANTICIPATED status
                List<HomeroomStudent> otherHomeroomStudents = homeroomStudentRepository.findByStudentAndStatus(student, HomeroomStatus.ANTICIPATED);
                
                if (!otherHomeroomStudents.isEmpty() && 
                    otherHomeroomStudents.stream().noneMatch(hs -> hs.getId().equals(homeroomStudent.getId()))) {
                    throw new BadRequestException("Student is already active in another homeroom");
                }
            }
            
            // Update the status
            homeroomStudent.setStatus(status);
            
            homeroomStudentRepository.save(homeroomStudent);
            
            log.info("Updated homeroom student status: homeroom id: {}, student id: {}, new status: {}", 
                homeroomId, studentId, status);
            
            return BaseResponse.accepted(null, "Homeroom student status updated successfully");
        } catch (ResourceNotFoundException | BadRequestException e) {
            log.error("Error updating homeroom student status", e);
            return BaseResponse.error(HttpStatus.BAD_REQUEST.value(), e.getMessage(), e, null);
        } catch (Exception e) {
            log.error("Failed to update homeroom student status", e);
            return BaseResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                "Failed to update homeroom student status", 
                e, 
                null
            );
        }
    }

    /**
     * Maps a Homeroom entity to HomeroomListResponse
     */
    private HomeroomListResponse mapToListResponse(Homeroom homeroom, Long studentCount) {
        return HomeroomListResponse.builder()
            .id(homeroom.getId())
            .name(homeroom.getName())
            .teacherId(homeroom.getTeacher().getId())
            .teacherName(homeroom.getTeacher().getUser().getFullName())
            .studentCount(studentCount.intValue())
            .createdAt(homeroom.getCreatedAt())
            .updatedAt(homeroom.getUpdatedAt())
            .build();
    }
    
    /**
     * Maps a Homeroom entity to HomeroomDetailResponse
     */
    private HomeroomDetailResponse mapToDetailResponse(Homeroom homeroom) {
        // Count students by status
        Map<HomeroomStatus, Long> statusCounts = homeroom.getStudents().stream()
            .collect(Collectors.groupingBy(
                HomeroomStudent::getStatus,
                Collectors.counting()
            ));
        
        // Map HomeroomStudents to response objects
        List<HomeroomStudentResponse> studentResponses = homeroom.getStudents().stream()
            .map(this::mapToStudentResponse)
            .collect(Collectors.toList());
        
        return HomeroomDetailResponse.builder()
            .id(homeroom.getId())
            .name(homeroom.getName())
            .teacherId(homeroom.getTeacher().getId())
            .teacherName(homeroom.getTeacher().getUser().getFullName())
            .totalStudents(homeroom.getStudents().size())
            .anticipatedStudents(statusCounts.getOrDefault(HomeroomStatus.ANTICIPATED, 0L).intValue())
            .expelledStudents(statusCounts.getOrDefault(HomeroomStatus.EXPELLED, 0L).intValue())
            .graduatedStudents(statusCounts.getOrDefault(HomeroomStatus.GRADUATED, 0L).intValue())
            .createdAt(homeroom.getCreatedAt())
            .updatedAt(homeroom.getUpdatedAt())
            .students(studentResponses)
            .build();
    }
    
    /**
     * Maps a HomeroomStudent entity to HomeroomStudentResponse
     */
    private HomeroomStudentResponse mapToStudentResponse(HomeroomStudent homeroomStudent) {
        return HomeroomStudentResponse.builder()
            .id(homeroomStudent.getId())
            .studentId(homeroomStudent.getStudent().getId())
            .studentName(homeroomStudent.getStudent().getUser().getFullName())
            .studentCode(homeroomStudent.getStudent().getUser().getAccount().getUsername())
            .status(homeroomStudent.getStatus())
            .statusName(getStatusName(homeroomStudent.getStatus()))
            .createdAt(homeroomStudent.getCreatedAt())
            .updatedAt(homeroomStudent.getUpdatedAt())
            .build();
    }
    
    /**
     * Converts HomeroomStatus enum to human-readable string
     */
    private String getStatusName(HomeroomStatus status) {
        if (status == null) {
            return "";
        }
        
        switch (status) {
            case ANTICIPATED:
                return "Currently Enrolled";
            case EXPELLED:
                return "Expelled";
            case GRADUATED:
                return "Graduated";
            default:
                return status.toString();
        }
    }
}