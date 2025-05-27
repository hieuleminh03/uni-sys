package service.teacher;

import dto.request.admin.CreateClassRequest;
import dto.response.BaseResponse;
import dto.response.admin.ClassDetailResponse;
import dto.response.admin.ClassListResponse;
import exception.ConflictTimeException;
import exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.*;
import model.Class;
import model.enums.AttendanceStatus;
import model.enums.ClassStudentStatus;
import model.enums.ExaminationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.*;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherClassServiceImpl {
        private final ClassRepository classRepository;
        private final AccountRepository accountRepository;
        private final UserRepository userRepository;
        private final TeacherRepository teacherRepository;
        private final ClassStudentRepository classStudentRepository;
        private final StudentExaminationRepository studentExaminationRepository;
        private final AttendanceHistoryRepository attendanceHistoryRepository;



        public BaseResponse<List<ClassListResponse>> getAllClasses(int page, int size) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                Account account = accountRepository.findByUsername(auth.getName())
                        .orElseThrow(() -> new RuntimeException("Account not found"));
                User user = userRepository.findByAccount(account)
                        .orElseThrow(() -> new RuntimeException("User not found"));
                Teacher teacher = teacherRepository.findByUser(user)
                        .orElseThrow(() -> new RuntimeException("Teacher not found"));
                Pageable pageable = Pageable.ofSize(size).withPage(page);
                Page<Class> classPage = classRepository.findClassByTeacher(teacher ,pageable);
                if (classPage.isEmpty()) {
                        return BaseResponse.ok(
                                        List.of(),
                                        "No class found",
                                        new util.Paging(page, size, 0, 0));
                }
                List<ClassListResponse> classListResponses = classPage.getContent().stream()
                                .map(this::mapToClassListResponse)
                                .toList();
                return BaseResponse.ok(
                                classListResponses,
                                "Class list retrieved successfully",
                                new util.Paging(page, size, classPage.getTotalElements(), classPage.getTotalPages()));
        }

        private ClassListResponse mapToClassListResponse(Class classEntity) {
                Integer studentCount = classRepository.countByStudent(classEntity.getId());
                return ClassListResponse.builder()
                                .id(classEntity.getId())
                                .className(classEntity.getClassName())
                                .teacherId(classEntity.getTeacher().getId())
                                .teacherFullName(classEntity.getTeacher().getUser().getFullName())
                                .teacherEmail(classEntity.getTeacher().getUser().getEmail())
                                .teacherAvatarUrl(classEntity.getTeacher().getUser().getAvatarUrl())
                                .subjectId(classEntity.getSubject().getId())
                                .subjectName(classEntity.getSubject().getName())
                                .startDate(classEntity.getStartDate())
                                .endDate(classEntity.getEndDate())
                                .description(classEntity.getDescription())
                                .tuition(classEntity.getTuition())
                                .tuitionDueDate(classEntity.getTuitionDueDate())
                                .finalTermWeight(classEntity.getFinalTermWeight())
                                .dayOfWeek(classEntity.getSchedules().getDayOfWeek())
                                .periodStart(classEntity.getSchedules().getPeriodStart())
                                .periodEnd(classEntity.getSchedules().getPeriodEnd())
                                .room(classEntity.getSchedules().getRoom())
                                .studentCount(studentCount)
                                .build();
        }

        public ClassDetailResponse getClassDetailsById(Long id) {
                Class classEntity = classRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Class not found"));
                List<ClassStudent> classStudents = classStudentRepository.findByClassEntity(classEntity);
                Integer studentCount = classRepository.countByStudent(id);
                return ClassDetailResponse.builder()
                        .id(classEntity.getId())
                        .className(classEntity.getClassName())
                        .teacherId(classEntity.getTeacher().getId())
                        .teacherFullName(classEntity.getTeacher().getUser().getFullName())
                        .teacherEmail(classEntity.getTeacher().getUser().getEmail())
                        .teacherAvatarUrl(classEntity.getTeacher().getUser().getAvatarUrl())
                        .subjectId(classEntity.getSubject().getId())
                        .subjectCode(classEntity.getSubject().getCode())
                        .subjectName(classEntity.getSubject().getName())
                        .startDate(classEntity.getStartDate())
                        .endDate(classEntity.getEndDate())
                        .description(classEntity.getDescription())
                        .tuition(classEntity.getTuition())
                        .tuitionDueDate(classEntity.getTuitionDueDate())
                        .finalTermWeight(classEntity.getFinalTermWeight())
                        .dayOfWeek(classEntity.getSchedules().getDayOfWeek())
                        .periodStart(classEntity.getSchedules().getPeriodStart())
                        .periodEnd(classEntity.getSchedules().getPeriodEnd())
                        .room(classEntity.getSchedules().getRoom())
                        .studentCount(studentCount)
                        .absenceWarningThreshold(classEntity.getAbsenceWarningThreshold())
                        .absenceLimit(classEntity.getAbsenceLimit())
                        .classStudentResponses(classStudents.stream().map(classStudent ->
                        {
                                StudentExamination midtermExam = studentExaminationRepository.findByClassStudentAndExamination_Type(classStudent, ExaminationType.MIDTERM);
                                StudentExamination finalExam = studentExaminationRepository.findByClassStudentAndExamination_Type(classStudent, ExaminationType.FINAL);
                                return ClassDetailResponse.ClassStudentResponse.builder()
                                        .id(classStudent.getId())
                                        .fullName(classStudent.getStudent().getUser().getFullName())
                                        .email(classStudent.getStudent().getUser().getEmail())
                                        .avatarUrl(classStudent.getStudent().getUser().getAvatarUrl())
                                        .absenceCount(attendanceHistoryRepository.countByClassStudentAndStatusAndStatus(classStudent,
                                                AttendanceStatus.ABSENT, AttendanceStatus.LATE))
                                        .midtermGrade(midtermExam != null ? midtermExam.getGrade() : null)
                                        .isAbsentMidterm(midtermExam != null && midtermExam.isAbsent())
                                        .finalTermGrade(finalExam != null ? finalExam.getGrade() : null)
                                        .isAbsentFinalTerm(finalExam != null && finalExam.isAbsent())
                                        .build();


                        }).toList())
                        .build();
        }


}
