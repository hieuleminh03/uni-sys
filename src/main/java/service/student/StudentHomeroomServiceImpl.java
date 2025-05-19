package service.student;

import dto.response.BaseResponse;
import dto.response.student.StudentHomeroomClassmateResponse;
import dto.response.student.StudentHomeroomDetailResponse;
import dto.response.student.StudentHomeroomListResponse;
import exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.Homeroom;
import model.HomeroomStudent;
import model.Student;
import model.enums.HomeroomStatus;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.HomeroomRepository;
import repository.HomeroomStudentRepository;
import repository.StudentRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentHomeroomServiceImpl {

    private final HomeroomRepository homeroomRepository;
    private final HomeroomStudentRepository homeroomStudentRepository;
    private final StudentRepository studentRepository;

    /**
     * Get all homerooms for the current student
     */
    @Transactional(readOnly = true)
    public BaseResponse<List<StudentHomeroomListResponse>> getAllHomerooms() {
        try {
            // Get current authenticated student
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            Student student = studentRepository.findByUserAccountUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
            
            // Get all homerooms for this student
            List<HomeroomStudent> homeroomStudents = homeroomStudentRepository.findByStudentIdWithHomeroom(student.getId());
            
            if (homeroomStudents.isEmpty()) {
                return BaseResponse.ok(
                    List.of(),
                    "No homerooms found for this student"
                );
            }
            
            List<StudentHomeroomListResponse> responseList = homeroomStudents.stream()
                .map(this::mapToListResponse)
                .collect(Collectors.toList());
            
            return BaseResponse.ok(
                responseList,
                "Homerooms retrieved successfully"
            );
        } catch (ResourceNotFoundException e) {
            log.error("Student not found", e);
            return BaseResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage(), e, null);
        } catch (Exception e) {
            log.error("Failed to retrieve homerooms for student", e);
            return BaseResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                "Failed to retrieve homerooms", 
                e, 
                null
            );
        }
    }

    /**
     * Get homeroom details by ID for the current student
     */
    @Transactional(readOnly = true)
    public BaseResponse<StudentHomeroomDetailResponse> getHomeroomById(Long id) {
        try {
            // Get current authenticated student
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            Student student = studentRepository.findByUserAccountUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
            
            // Find the homeroom
            Homeroom homeroom = homeroomRepository.findByIdWithTeacherAndStudents(id)
                .orElseThrow(() -> new ResourceNotFoundException("Homeroom not found with id: " + id));
            
            // Check if student is in this homeroom
            HomeroomStudent homeroomStudent = homeroomStudentRepository.findByHomeroomAndStudent(homeroom, student)
                .orElseThrow(() -> new ResourceNotFoundException("You are not a member of this homeroom"));
            
            StudentHomeroomDetailResponse response = mapToDetailResponse(homeroom, homeroomStudent);
            
            return BaseResponse.ok(response, "Homeroom details retrieved successfully");
        } catch (ResourceNotFoundException e) {
            log.error("Error retrieving homeroom details", e);
            return BaseResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage(), e, null);
        } catch (Exception e) {
            log.error("Failed to retrieve homeroom details", e);
            return BaseResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                "Failed to retrieve homeroom details", 
                e, 
                null
            );
        }
    }

    /**
     * Maps a HomeroomStudent entity to StudentHomeroomListResponse
     */
    private StudentHomeroomListResponse mapToListResponse(HomeroomStudent homeroomStudent) {
        return StudentHomeroomListResponse.builder()
            .id(homeroomStudent.getHomeroom().getId())
            .name(homeroomStudent.getHomeroom().getName())
            .teacherId(homeroomStudent.getHomeroom().getTeacher().getId())
            .teacherName(homeroomStudent.getHomeroom().getTeacher().getUser().getFullName())
            .status(homeroomStudent.getStatus())
            .statusName(getStatusName(homeroomStudent.getStatus()))
            .createdAt(homeroomStudent.getCreatedAt())
            .updatedAt(homeroomStudent.getUpdatedAt())
            .build();
    }

    /**
     * Maps a Homeroom entity to StudentHomeroomDetailResponse
     */
    private StudentHomeroomDetailResponse mapToDetailResponse(Homeroom homeroom, HomeroomStudent currentStudentHomeroom) {
        // Count students by status
        Map<HomeroomStatus, Long> statusCounts = homeroom.getStudents().stream()
            .collect(Collectors.groupingBy(
                HomeroomStudent::getStatus,
                Collectors.counting()
            ));
        
        // Map classmates (excluding current student)
        List<StudentHomeroomClassmateResponse> classmateResponses = homeroom.getStudents().stream()
            .filter(hs -> !hs.getStudent().getId().equals(currentStudentHomeroom.getStudent().getId()))
            .map(this::mapToClassmateResponse)
            .collect(Collectors.toList());
        
        return StudentHomeroomDetailResponse.builder()
            .id(homeroom.getId())
            .name(homeroom.getName())
            .teacherId(homeroom.getTeacher().getId())
            .teacherName(homeroom.getTeacher().getUser().getFullName())
            .status(currentStudentHomeroom.getStatus())
            .statusName(getStatusName(currentStudentHomeroom.getStatus()))
            .totalStudents(homeroom.getStudents().size())
            .createdAt(homeroom.getCreatedAt())
            .updatedAt(homeroom.getUpdatedAt())
            .classmates(classmateResponses)
            .build();
    }

    /**
     * Maps a HomeroomStudent entity to StudentHomeroomClassmateResponse
     */
    private StudentHomeroomClassmateResponse mapToClassmateResponse(HomeroomStudent homeroomStudent) {
        return StudentHomeroomClassmateResponse.builder()
            .id(homeroomStudent.getId())
            .studentId(homeroomStudent.getStudent().getId())
            .studentName(homeroomStudent.getStudent().getUser().getFullName())
            .studentCode(homeroomStudent.getStudent().getUser().getAccount().getUsername())
            .status(homeroomStudent.getStatus())
            .statusName(getStatusName(homeroomStudent.getStatus()))
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