package service.teacher;

import dto.request.teacher.TeacherAttendanceCreateRequest;
import dto.response.teacher.AttendanceListResponse;
import exception.ConflictTimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.Attendance;
import model.AttendanceHistory;
import model.Class;
import model.ClassStudent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.AttendanceHistoryRepository;
import repository.AttendanceRepository;
import repository.ClassRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeacherAttendanceServiceImpl {
    private final AttendanceRepository attendanceRepository;
    private final AttendanceHistoryRepository attendanceHistoryRepository;
    private final ClassRepository classRepository;
    public List<AttendanceListResponse> getAttendanceByClass(Long classId) {
        Class classEntity = classRepository.findAttendenceById(classId)
                .orElseThrow(() -> new IllegalArgumentException("Class not found with id: " + classId));
        List<ClassStudent> classStudents=classEntity.getStudents();
        return classStudents.stream()
                .map(classStudent -> {
                    List<AttendanceHistory> attendanceHistories = attendanceHistoryRepository.findByClassStudent(classStudent);


                    return AttendanceListResponse.builder()
                            .classStudentId(classStudent.getId())
                            .studentName(classStudent.getStudent().getUser().getFullName())
                            .studentEmail(classStudent.getStudent().getUser().getEmail())
                            .attendanceHistories(attendanceHistories.stream().map(
                                    attendanceHistory -> AttendanceListResponse.AttendanceHistoriesResponse.builder()
                                            .attendanceId(attendanceHistory.getAttendance().getId())
                                            .date(attendanceHistory.getAttendance().getDate())
                                            .attendanceType(attendanceHistory.getAttendance().getType())
                                            .attendanceStatus(attendanceHistory.getStatus())
                                            .build()).toList()
                            ).build();
                })
                .toList();
    }
    @Transactional
    public void createAttendance(Long classId, TeacherAttendanceCreateRequest teacherAttendanceCreateRequest) {
        Class classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("Class not found with id: " + classId));

        if(classEntity.getStartDate().after(teacherAttendanceCreateRequest.getDate()) ||
                classEntity.getEndDate().before(teacherAttendanceCreateRequest.getDate())) {
            throw new ConflictTimeException("Attendance date is out of class date range");
        }
        Attendance attendance = Attendance.builder()
                .classEntity(classEntity)
                .type(teacherAttendanceCreateRequest.getAttendanceType())
                .date(teacherAttendanceCreateRequest.getDate())
                .build();

        attendanceRepository.save(attendance);


        for (TeacherAttendanceCreateRequest.AttendanceResult result : teacherAttendanceCreateRequest.getAttendanceResults()) {
            ClassStudent classStudent = classEntity.getStudents().stream()
                    .filter(cs -> cs.getId().equals(result.getClassStudentId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Class student not found with id: " + result.getClassStudentId()));

            AttendanceHistory attendanceHistory = AttendanceHistory.builder()
                    .attendance(attendance)
                    .classStudent(classStudent)
                    .status(result.getAttendanceStatus())
                    .build();

            attendanceHistoryRepository.save(attendanceHistory);
        }
    }
}
