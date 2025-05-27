package service.student;

import dto.response.BaseResponse;
import dto.response.admin.ScheduleExamListResponse;
import exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.Class;
import model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import repository.ClassRepository;
import repository.ExamRepository;
import repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentExamServiceImpl {
    private final ClassRepository classRepository;
    private final StudentRepository studentRepository;
    private final ExamRepository examRepository;
    public BaseResponse<List<ScheduleExamListResponse>> scheduleExam(int page, int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Student student = studentRepository.findByUserAccountUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<Class> classes = classRepository.findAllAddExamAndStudent(pageable,student);
        List<ScheduleExamListResponse> scheduleExamListResponses = new ArrayList<>();
        for (Class classEntity : classes) {
            ScheduleExamListResponse scheduleExamListResponse;
            if (!classEntity.getExaminations().isEmpty()) {
                scheduleExamListResponse = ScheduleExamListResponse.builder()
                        .classId(classEntity.getId())
                        .className(classEntity.getClassName())
                        .classScheduleResponses(examRepository.findByClassEntity(classEntity).stream().map(examination ->
                                ScheduleExamListResponse.ClassScheduleResponse.builder()
                                        .examinationId(examination.getId())
                                        .examinationType(examination.getType())
                                        .date(examination.getDate())
                                        .build()).toList())
                        .build();
            }else{
                scheduleExamListResponse = ScheduleExamListResponse.builder()
                        .classId(classEntity.getId())
                        .className(classEntity.getClassName())
                        .classScheduleResponses(new ArrayList<>())
                        .build();
            }
            scheduleExamListResponses.add(scheduleExamListResponse);

        }
        if(classes.isEmpty()) {
            return BaseResponse.ok(null, "No exams scheduled",new util.Paging(page, size,0,0));
        }
        return BaseResponse.ok(scheduleExamListResponses, "Exams scheduled successfully", new util.Paging(page, size, classes.getTotalElements(), classes.getTotalPages()));
    }
}
