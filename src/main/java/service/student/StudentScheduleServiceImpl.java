package service.student;

import dto.response.student.StudentScheduleResponse;
import dto.response.teacher.TeacherScheduleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.*;
import model.Class;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import repository.*;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentScheduleServiceImpl {
    private final ClassRepository classRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final ClassStudentRepository classStudentRepository;

    public List<StudentScheduleResponse> getScheduleByMonth(Integer month, Integer year) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = accountRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        User user = userRepository.findByAccount(account)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Student student = studentRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Student not found"));


        List<ClassStudent> classes = classStudentRepository.findByStudentAndMonthAndYear(student, month, year);
        if (classes.isEmpty()) {
            return List.of();
        }
        return classes.stream().map(
                classEntity -> StudentScheduleResponse.builder()
                        .classId(classEntity.getClassEntity().getId())
                        .teacherName(classEntity.getClassEntity().getTeacher().getUser().getFullName())
                        .teacherEmail(classEntity.getClassEntity().getTeacher().getUser().getEmail())
                        .className(classEntity.getClassEntity().getClassName())
                        .startDate(classEntity.getClassEntity().getStartDate())
                        .endDate(classEntity.getClassEntity().getEndDate())
                        .dayOfWeek(classEntity.getClassEntity().getSchedules().getDayOfWeek())
                        .periodStart(classEntity.getClassEntity().getSchedules().getPeriodStart())
                        .periodEnd(classEntity.getClassEntity().getSchedules().getPeriodEnd())
                        .room(classEntity.getClassEntity().getSchedules().getRoom())
                        .subjectName(classEntity.getClassEntity().getSubject().getName())
                        .subjectCode(classEntity.getClassEntity().getSubject().getCode())
                        .build()
        ).toList();
    }
}
