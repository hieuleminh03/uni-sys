package service.admin;

import dto.request.admin.AdminUpdateGradeRequest;
import dto.request.admin.CreateExamToClassRequest;
import dto.response.BaseResponse;
import dto.response.admin.ExamListResponse;
import dto.response.admin.ScheduleExamListResponse;
import exception.ConflictTimeException;
import exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.Class;
import model.ClassStudent;
import model.Examination;
import model.StudentExamination;
import model.enums.AttendanceStatus;
import model.enums.ExaminationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.*;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminExamServiceImpl {
    private final ExamRepository examRepository;
    private final ClassRepository classRepository;
    private final ClassStudentRepository classStudentRepository;
    private final AttendanceHistoryRepository attendanceHistoryRepository;
    private final StudentExaminationRepository studentExaminationRepository;

    public ExamListResponse getExamByClass(Long classId, ExaminationType examinationType) {
        Class classEntity = classRepository.findAttendenceById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + classId));
        Examination examination = examRepository.findByClassEntityAndType(classEntity, examinationType);
        if (examination == null) {
            List<ClassStudent> classStudents = classEntity.getStudents();
            return ExamListResponse.builder()
                    .attendanceResponses(classStudents.stream().map(
                            classStudent -> {
                                Integer absenceCount = attendanceHistoryRepository.findByTypeAndClassStudent(
                                        AttendanceStatus.ABSENT, AttendanceStatus.LATE, classStudent
                                );
                                return ExamListResponse.AttendanceResponse.builder()
                                        .classStudentId(classStudent.getId())
                                        .studentName(classStudent.getStudent().getUser().getFullName())
                                        .studentEmail(classStudent.getStudent().getUser().getEmail())
                                        .absenceAttendanceCount(absenceCount)
                                        .totalAttendanceCount(classEntity.getAbsenceLimit())
                                        .build();
                            }
                    ).toList())
                    .build();
        }
        return ExamListResponse.builder()
                .examinationId(examination.getId())
                .date(examination.getDate())
                .examinationType(examination.getType())
                .examStudentResponses(examination.getStudentExaminations().stream().map(
                        studentExamination -> ExamListResponse.ExamStudentResponse.builder()
                                .studentExaminationId(studentExamination.getId())
                                .studentName(studentExamination.getClassStudent().getStudent().getUser().getFullName())
                                .studentEmail(studentExamination.getClassStudent().getStudent().getUser().getEmail())
                                .grade(studentExamination.getGrade())
                                .isAbsent(studentExamination.isAbsent())
                                .build()
                ).toList())
                .build();
    }

    @Transactional
    public void createExam(Long classId, CreateExamToClassRequest createExamToClassRequest) {
        Class classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + classId));
        if (classEntity.getStartDate().after(createExamToClassRequest.getDate()) ||
                classEntity.getEndDate().before(createExamToClassRequest.getDate())) {
            throw new ConflictTimeException("Bài thi không được tạo ngoài khoảng thời gian của lớp học");
        }
        if(createExamToClassRequest.getExaminationType()==ExaminationType.FINAL){
            if(!examRepository.existsByClassEntity(classEntity)){
                throw new ConflictTimeException("Bài thi cuối kỳ phải được tạo sau khi có bài thi giữa kỳ");
            }else{
                Examination midtermExam = examRepository.findByClassEntityAndType(classEntity, ExaminationType.MIDTERM);
                if (midtermExam == null) {
                    throw new ConflictTimeException("Bài thi giữa kỳ chưa được tạo");
                }
                if(midtermExam.getDate().after(createExamToClassRequest.getDate())) {
                    throw new ConflictTimeException("Bài thi cuối kỳ không được tạo trước bài thi giữa kỳ");
                }
            }
        }
        List<ClassStudent> classStudents = classStudentRepository.findAllById(createExamToClassRequest.getExamStudentRequests().stream().map(CreateExamToClassRequest.ExamStudentRequest::getClassStudentId).toList());
        if (classStudents.size() != createExamToClassRequest.getExamStudentRequests().size()) {
            throw new ResourceNotFoundException("Some class students not found");
        }
        Examination examination = Examination.builder()
                .classEntity(classEntity)
                .type(createExamToClassRequest.getExaminationType())
                .date(createExamToClassRequest.getDate())
                .build();
        examRepository.save(examination);
        classStudents.forEach(classStudent -> {
                    StudentExamination studentExamination = StudentExamination.builder()
                            .classStudent(classStudent)
                            .examination(examination)
                            .isAbsent(createExamToClassRequest.getExamStudentRequests().stream()
                                    .filter(request -> request.getClassStudentId().equals(classStudent.getId()))
                                    .map(CreateExamToClassRequest.ExamStudentRequest::getIsAbsent)
                                    .findFirst().get())
                            .build();
                    studentExaminationRepository.save(studentExamination);

                }
        );

    }

    @Transactional
    public void updateExam(AdminUpdateGradeRequest adminUpdateGradeRequest) {
        adminUpdateGradeRequest.getUpdateGradeRequests().forEach(request -> {
            StudentExamination studentExamination = studentExaminationRepository.findById(request.getStudentExaminationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student examination not found with id: " + request.getStudentExaminationId()));
            if (request.getGrade() != null && request.getGrade() >= 0 && request.getGrade() <= 10 && !studentExamination.isAbsent()) {
                studentExamination.setGrade(request.getGrade());
                studentExaminationRepository.save(studentExamination);
            }

        });
    }

    public BaseResponse<List<ScheduleExamListResponse>> scheduleExam(int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<Class> classes = classRepository.findAllAddExam(pageable);
        List<ScheduleExamListResponse> scheduleExamListResponses = new ArrayList<>();
        for (Class classEntity : classes) {
            ScheduleExamListResponse scheduleExamListResponse;
            if (!classEntity.getExaminations().isEmpty()) {
                scheduleExamListResponse = ScheduleExamListResponse.builder()
                        .classId(classEntity.getId())
                        .className(classEntity.getClassName())
                        .classScheduleResponses(classEntity.getExaminations().stream().map(examination ->
                                ScheduleExamListResponse.ClassScheduleResponse.builder()
                                        .examinationId(examination.getId())
                                        .examinationType(examination.getType())
                                        .date(examination.getDate())
                                        .build()).toList())
                        .build();
            } else {
                scheduleExamListResponse = ScheduleExamListResponse.builder()
                        .classId(classEntity.getId())
                        .className(classEntity.getClassName())
                        .classScheduleResponses(new ArrayList<>())
                        .build();
            }
            scheduleExamListResponses.add(scheduleExamListResponse);

        }
        if (classes.isEmpty()) {
            return BaseResponse.ok(null, "No exams scheduled", new util.Paging(page, size, 0, 0));
        }
        return BaseResponse.ok(scheduleExamListResponses, "Exams scheduled successfully", new util.Paging(page, size, classes.getTotalElements(), classes.getTotalPages()));
    }

    public void deleteExam(Long examinationId) {
        examRepository.deleteById(examinationId);
    }
}

