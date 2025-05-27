package service.teacher;

import dto.request.admin.HomeroomStatusResquest;
import dto.response.BaseResponse;
import dto.response.teacher.TeacherHomeroomDetailResponse;
import dto.response.teacher.TeacherHomeroomListResponse;
import dto.response.teacher.TeacherHomeroomStudentResponse;
import exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.Homeroom;
import model.HomeroomStudent;
import model.Student;
import model.Teacher;
import model.enums.HomeroomStatus;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.HomeroomRepository;
import repository.HomeroomStudentRepository;
import repository.StudentRepository;
import repository.TeacherRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherHomeroomServiceImpl {

    private final HomeroomRepository homeroomRepository;
    private final HomeroomStudentRepository homeroomStudentRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;


    /**
     * Get the current teacher's homeroom details
     */
    @Transactional(readOnly = true)
    public BaseResponse<TeacherHomeroomDetailResponse> getHomeroom() {
        try {
            // Get current authenticated teacher
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            Teacher teacher = teacherRepository.findByUserAccountUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
            
            // Find the homeroom
            Homeroom homeroom = homeroomRepository.findByIdWithTeacherAndStudents(teacher.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Homeroom not found with teacherid: " + teacher.getId()));
            
            TeacherHomeroomDetailResponse response = mapToDetailResponse(homeroom);
            
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
     * Maps a Homeroom entity to TeacherHomeroomListResponse
     */
    private TeacherHomeroomListResponse mapToListResponse(Homeroom homeroom, Map<HomeroomStatus, Long> statusCounts) {
        return TeacherHomeroomListResponse.builder()
            .id(homeroom.getId())
            .name(homeroom.getName())
            .studentCount(statusCounts != null ? statusCounts.values().stream().mapToInt(Long::intValue).sum() : 0)
            .anticipatedStudents(statusCounts != null ? statusCounts.getOrDefault(HomeroomStatus.ANTICIPATED, 0L).intValue() : 0)
            .expelledStudents(statusCounts != null ? statusCounts.getOrDefault(HomeroomStatus.EXPELLED, 0L).intValue() : 0)
            .graduatedStudents(statusCounts != null ? statusCounts.getOrDefault(HomeroomStatus.GRADUATED, 0L).intValue() : 0)
            .createdAt(homeroom.getCreatedAt())
            .updatedAt(homeroom.getUpdatedAt())
            .build();
    }

    /**
     * Maps a Homeroom entity to TeacherHomeroomDetailResponse
     */
    private TeacherHomeroomDetailResponse mapToDetailResponse(Homeroom homeroom) {
        // Count students by status
        Map<HomeroomStatus, Long> statusCounts = homeroom.getStudents().stream()
            .collect(Collectors.groupingBy(
                HomeroomStudent::getStatus,
                Collectors.counting()
            ));
        
        // Map students to response objects
        List<TeacherHomeroomStudentResponse> studentResponses = homeroom.getStudents().stream()
            .map(this::mapToStudentResponse)
            .collect(Collectors.toList());
        
        return TeacherHomeroomDetailResponse.builder()
            .id(homeroom.getId())
            .name(homeroom.getName())
            .teacherName(homeroom.getTeacher().getUser().getFullName())
            .teacherId(homeroom.getTeacher().getId())
            .teacherEmail(homeroom.getTeacher().getUser().getEmail())
            .teacherAvatarUrl(homeroom.getTeacher().getUser().getAvatarUrl())
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
     * Maps a HomeroomStudent entity to TeacherHomeroomStudentResponse
     */
    private TeacherHomeroomStudentResponse mapToStudentResponse(HomeroomStudent homeroomStudent) {
        return TeacherHomeroomStudentResponse.builder()
            .id(homeroomStudent.getId())
            .studentId(homeroomStudent.getStudent().getId())
            .studentName(homeroomStudent.getStudent().getUser().getFullName())
            .studentAvatarUrl(homeroomStudent.getStudent().getUser().getAvatarUrl())
            .studentEmail(homeroomStudent.getStudent().getUser().getEmail())
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

    public BaseResponse<String> updateStatusStudent(Long studentId, HomeroomStatusResquest homeroomStatusResquest) {
        try {
            // Get current authenticated teacher
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            Teacher teacher = teacherRepository.findByUserAccountUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

            // Find the homeroom
            Homeroom homeroom = homeroomRepository.findByIdWithTeacherAndStudents(teacher.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Homeroom not found with id: " + teacher.getId()));

            Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

            // Find the homeroom student
            HomeroomStudent homeroomStudent = homeroomStudentRepository.findByHomeroomAndStudent(homeroom, student)
                .orElseThrow(() -> new ResourceNotFoundException("Homeroom not found with id: " + studentId));

            // Update the status
            homeroomStudent.setStatus(homeroomStatusResquest.getStatus());
            homeroomStudentRepository.save(homeroomStudent);

            return BaseResponse.accepted("Student status updated successfully", "Status updated successfully");
        } catch (ResourceNotFoundException e) {
            log.error("Error updating student status", e);
            return BaseResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage(), e, null);
        } catch (Exception e) {
            log.error("Failed to update student status", e);
            return BaseResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Failed to update student status",
                e,
                null
            );
        }
    }
}