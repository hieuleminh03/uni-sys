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
     * Get the current student's homeroom details
     */
    @Transactional(readOnly = true)
    public BaseResponse<StudentHomeroomDetailResponse> getHomeroom() {
        try {
            // Get current authenticated student
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            Student student = studentRepository.findByUserAccountUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

            // Check if student is in this homeroom
            HomeroomStudent homeroomStudent = homeroomStudentRepository.findByStudent(student)
                    .orElseThrow(() -> new ResourceNotFoundException("Homeroom not found for studentId: " + student.getId()));

            Homeroom homeroom = homeroomStudentRepository.findByHomeroomStudent(homeroomStudent);

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
                .map(this::mapToClassmateResponse)
                .collect(Collectors.toList());

        return StudentHomeroomDetailResponse.builder()
                .id(homeroom.getId())
                .name(homeroom.getName())
                .teacherId(homeroom.getTeacher().getId())
                .teacherEmail(homeroom.getTeacher().getUser().getEmail())
                .teacherAvatarUrl(homeroom.getTeacher().getUser().getAvatarUrl())
                .teacherName(homeroom.getTeacher().getUser().getFullName())
                .status(currentStudentHomeroom.getStatus())
                .anticipatedStudents(statusCounts.getOrDefault(HomeroomStatus.ANTICIPATED, 0L).intValue())
                .expelledStudents(statusCounts.getOrDefault(HomeroomStatus.EXPELLED, 0L).intValue())
                .graduatedStudents(statusCounts.getOrDefault(HomeroomStatus.GRADUATED, 0L).intValue())
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
                .studentAvatarUrl(homeroomStudent.getStudent().getUser().getAvatarUrl())
                .studentEmail(homeroomStudent.getStudent().getUser().getEmail())
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