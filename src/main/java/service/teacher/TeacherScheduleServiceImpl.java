package service.teacher;

import dto.response.BaseResponse;
import dto.response.teacher.TeacherScheduleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.Account;
import model.Class;
import model.Teacher;
import model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import repository.ClassRepository;
import repository.AccountRepository;
import repository.TeacherRepository;
import repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherScheduleServiceImpl {
    private final ClassRepository classRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;

    public List<TeacherScheduleResponse> getScheduleByMonth(Integer month, Integer year) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = accountRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        User user = userRepository.findByAccount(account)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Teacher teacher = teacherRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        List<Class> classes = classRepository.findByAndTeacherMonthAndYear(teacher, month, year);
        if (classes.isEmpty()) {
            return List.of();
        }
        return classes.stream().map(
                classEntity -> TeacherScheduleResponse.builder()
                        .classId(classEntity.getId())
                        .className(classEntity.getClassName())
                        .startDate(classEntity.getStartDate())
                        .endDate(classEntity.getEndDate())
                        .dayOfWeek(classEntity.getSchedules().getDayOfWeek())
                        .periodStart(classEntity.getSchedules().getPeriodStart())
                        .periodEnd(classEntity.getSchedules().getPeriodEnd())
                        .room(classEntity.getSchedules().getRoom())
                        .subjectName(classEntity.getSubject().getName())
                        .subjectCode(classEntity.getSubject().getCode())
                        .build()
        ).toList();
    }
}
