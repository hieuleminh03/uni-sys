package service.admin;

import dto.request.admin.CreateClassRequest;
import dto.response.BaseResponse;
import dto.response.admin.ClassDetailResponse;
import dto.response.admin.ClassListResponse;
import exception.ConflictTimeException;
import exception.ResourceNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.*;
import model.Class;
import model.enums.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.*;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminClassServiceImpl {
    private final AttendanceHistoryRepository attendanceHistoryRepository;
    private final TuitionRepository tuitionRepository;
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final ClassRepository classRepository;
    private final ClassStudentRepository classStudentRepository;
    private final ClassScheduleRepository classScheduleRepository;
    private final StudentRepository studentRepository;
    private final ExamRepository examRepository;
    private final StudentExaminationRepository studentExaminationRepository;

    @Transactional
    public void createClass(CreateClassRequest createClassRequest) {
        Teacher teacher = teacherRepository.findById(createClassRequest.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        Subject subject = subjectRepository.findById(createClassRequest.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));

        if (createClassRequest.getStartDate().after(createClassRequest.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
        if (createClassRequest.getTuitionDueDate().after(createClassRequest.getEndDate())) {
            throw new IllegalArgumentException("Tuition due date must be before end date");
        }
        if (createClassRequest.getStartDate().after(createClassRequest.getTuitionDueDate())) {
            throw new IllegalArgumentException("Tuition due date must be after start date");
        }
        if (createClassRequest.getAbsenceWarningThreshold() > createClassRequest.getAbsenceLimit()) {
            throw new IllegalArgumentException(
                    "Absence warning threshold must be less than or equal to absence limit");
        }
        if (createClassRequest.getPeriodStart() >= createClassRequest.getPeriodEnd()) {
            throw new IllegalArgumentException("Class start time must be before end time");
        }
        if (createClassRequest.getFinalTermWeight() > 1 || createClassRequest.getFinalTermWeight() < 0) {
            throw new IllegalArgumentException("Final term weight must be between 0 and 1");
        }

        List<Class> classList = classRepository.findByTeacher(teacher);
        classList = classList.stream()
                .filter(aClass -> !(aClass.getStartDate().after(createClassRequest.getEndDate())
                        || aClass.getEndDate().before(createClassRequest.getStartDate())))
                .filter(aClass -> {
                    ClassSchedule classSchedule = aClass.getSchedules();
                    return classSchedule.getDayOfWeek() == createClassRequest.getDayOfWeek()
                            && !(classSchedule.getPeriodStart() > createClassRequest
                            .getPeriodEnd()
                            || classSchedule.getPeriodEnd() < createClassRequest
                            .getPeriodStart());
                }).toList();
        if (!classList.isEmpty()) {
            throw new ConflictTimeException("Giáo viên đã dạy lớp khác");
        }

        List<Class> classes = classRepository.findBySchedule(createClassRequest.getRoom());
        classes = classes.stream()
                .filter(aClass -> !(aClass.getStartDate().after(createClassRequest.getEndDate())
                        || aClass.getEndDate().before(createClassRequest.getStartDate())))
                .filter(aClass -> {
                    ClassSchedule classSchedule = aClass.getSchedules();
                    return classSchedule.getDayOfWeek() == createClassRequest.getDayOfWeek()
                            && !(classSchedule.getPeriodStart() > createClassRequest
                            .getPeriodEnd()
                            || classSchedule.getPeriodEnd() < createClassRequest
                            .getPeriodStart());
                }).toList();
        if (!classes.isEmpty()) {
            throw new ConflictTimeException("Phòng học đã có lớp khác");
        }

        Class classEntity = Class.builder()
                .teacher(teacher)
                .subject(subject)
                .className(createClassRequest.getClassName())
                .startDate(createClassRequest.getStartDate())
                .endDate(createClassRequest.getEndDate())
                .description(createClassRequest.getDescription())
                .tuition(createClassRequest.getTuition())
                .tuitionDueDate(createClassRequest.getTuitionDueDate())
                .finalTermWeight(createClassRequest.getFinalTermWeight())
                .absenceWarningThreshold(createClassRequest.getAbsenceWarningThreshold())
                .absenceLimit(createClassRequest.getAbsenceLimit())
                .build();

        classRepository.save(classEntity);

        ClassSchedule classSchedule = ClassSchedule.builder()
                .classEntity(classEntity)
                .dayOfWeek(createClassRequest.getDayOfWeek())
                .periodStart(createClassRequest.getPeriodStart())
                .periodEnd(createClassRequest.getPeriodEnd())
                .room(createClassRequest.getRoom())
                .build();

        classScheduleRepository.save(classSchedule);

    }

    public BaseResponse<List<ClassListResponse>> getAllClasses(int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<Class> classPage = classRepository.findAll(pageable);
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
                                                    .tuitionStatus(classStudent.getTuitionRecord().getStatus())
                                                    .midtermGrade(midtermExam != null ? midtermExam.getGrade() : null)
                                                    .isAbsentMidterm(midtermExam != null && midtermExam.isAbsent())
                                                    .finalTermGrade(finalExam != null ? finalExam.getGrade() : null)
                                                    .isAbsentFinalTerm(finalExam != null && finalExam.isAbsent())
                                                    .build();


                                        }).toList())
                                .build();
    }

    @Transactional
    public void updateClass(Long id, CreateClassRequest updateClassRequest) {
        Class classEntity = classRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found"));

        Teacher teacher = teacherRepository.findById(updateClassRequest.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        Subject subject = subjectRepository.findById(updateClassRequest.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));

        if (updateClassRequest.getStartDate().after(updateClassRequest.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
        if (updateClassRequest.getTuitionDueDate().after(updateClassRequest.getEndDate())) {
            throw new IllegalArgumentException("Tuition due date must be before end date");
        }
        if (updateClassRequest.getStartDate().after(updateClassRequest.getTuitionDueDate())) {
            throw new IllegalArgumentException("Tuition due date must be after start date");
        }
        if (updateClassRequest.getAbsenceWarningThreshold() > updateClassRequest.getAbsenceLimit()) {
            throw new IllegalArgumentException(
                    "Absence warning threshold must be less than or equal to absence limit");
        }
        if (updateClassRequest.getPeriodStart() >= updateClassRequest.getPeriodEnd()) {
            throw new IllegalArgumentException("Class start time must be before end time");
        }
        if (updateClassRequest.getFinalTermWeight() > 1 || updateClassRequest.getFinalTermWeight() < 0) {
            throw new IllegalArgumentException("Final term weight must be between 0 and 1");
        }

        List<Class> classList = classRepository.findByTeacher(teacher);
        classList = classList.stream()
                .filter(aClass -> !(aClass.getStartDate().after(updateClassRequest.getEndDate())
                        || aClass.getEndDate().before(updateClassRequest.getStartDate())
                        || aClass.getId().equals(id)))
                .filter(aClass -> {
                    ClassSchedule classSchedule = aClass.getSchedules();
                    return classSchedule.getDayOfWeek() == updateClassRequest.getDayOfWeek()
                            && !(classSchedule.getPeriodStart() > updateClassRequest
                            .getPeriodEnd()
                            || classSchedule.getPeriodEnd() < updateClassRequest
                            .getPeriodStart());
                }).toList();
        if (!classList.isEmpty()) {
            throw new ConflictTimeException("Giáo viên đã dạy lớp khác");
        }

        List<Class> classes = classRepository.findBySchedule(updateClassRequest.getRoom());
        classes = classes.stream()
                .filter(aClass -> !(aClass.getStartDate().after(updateClassRequest.getEndDate())
                        || aClass.getEndDate().before(updateClassRequest.getStartDate())
                        || aClass.getId().equals(id)))
                .filter(aClass -> {
                    ClassSchedule classSchedule = aClass.getSchedules();
                    return classSchedule.getDayOfWeek() == updateClassRequest.getDayOfWeek()
                            && !(classSchedule.getPeriodStart() > updateClassRequest
                            .getPeriodEnd()
                            || classSchedule.getPeriodEnd() < updateClassRequest
                            .getPeriodStart());
                }).toList();
        if (!classes.isEmpty()) {
            throw new ConflictTimeException("Phòng học đã có lớp khác");
        }

        List<ClassStudent> classStudents = classEntity.getStudents();
        if (classStudents != null && !classStudents.isEmpty()) {
            throw new ResourceNotFoundException("Không thể cập nhật vì lớp đã có học sinh đăng ký");
        }


        classEntity.setTeacher(teacher);
        classEntity.setSubject(subject);
        classEntity.setClassName(updateClassRequest.getClassName());
        classEntity.setStartDate(updateClassRequest.getStartDate());
        classEntity.setEndDate(updateClassRequest.getEndDate());
        classEntity.setDescription(updateClassRequest.getDescription());
        classEntity.setTuition(updateClassRequest.getTuition());
        classEntity.setTuitionDueDate(updateClassRequest.getTuitionDueDate());
        classEntity.setFinalTermWeight(updateClassRequest.getFinalTermWeight());
        classEntity.setAbsenceWarningThreshold(updateClassRequest.getAbsenceWarningThreshold());
        classEntity.setAbsenceLimit(updateClassRequest.getAbsenceLimit());

        ClassSchedule classSchedule = classEntity.getSchedules();
        classSchedule.setDayOfWeek(updateClassRequest.getDayOfWeek());
        classSchedule.setPeriodStart(updateClassRequest.getPeriodStart());
        classSchedule.setPeriodEnd(updateClassRequest.getPeriodEnd());
        classSchedule.setRoom(updateClassRequest.getRoom());
        classSchedule.setClassEntity(classEntity);
        classScheduleRepository.save(classSchedule);

        classRepository.save(classEntity);
    }

    @Transactional
    public void addClassStudentsBatch(Long id, List<Long> studentIds) {
        Class classEntity = classRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found"));
        if (examRepository.existsByClassEntity(classEntity)) {
            throw new ConflictTimeException("Không thể thêm học sinh vào lớp đã có bài thi");
        }
        List<Student> students = studentRepository.findAllById(studentIds);
        students.forEach(student -> {
            List<Class> classes = classRepository.findByStudent(student);
            classes = classes.stream()
                    .filter(aClass -> !(aClass.getStartDate().after(classEntity.getEndDate())
                            || aClass.getEndDate().before(classEntity.getStartDate())))
                    .filter(aClass -> {
                        ClassSchedule classSchedule = aClass.getSchedules();
                        return classSchedule.getDayOfWeek() == classEntity.getSchedules()
                                .getDayOfWeek()
                                && !(classSchedule.getPeriodStart() > classEntity
                                .getSchedules().getPeriodEnd()
                                || classSchedule.getPeriodEnd() < classEntity
                                .getSchedules()
                                .getPeriodStart());
                    }).toList();
            if (!classes.isEmpty()) {
                throw new ConflictTimeException("Học sinh đã có lớp khác trong thời gian này");
            }
        });
        if (students.isEmpty()) {
            throw new ResourceNotFoundException("No students found with the provided IDs");
        }
        List<ClassStudent> classStudents = students.stream()
                .map(student -> ClassStudent.builder()
                        .classEntity(classEntity)
                        .student(student)
                        .build())
                .toList();
        classEntity.getStudents().addAll(classStudents);
        classStudents.forEach(classStudent -> {
            classStudent.setStatus(ClassStudentStatus.ENROLLED);
            classStudent.setClassEntity(classEntity);
        });
        classStudentRepository.saveAll(classStudents);
        List<TuitionRecord> tuitionRecords = classStudents.stream()
                .map(classStudent -> TuitionRecord.builder()
                        .classStudent(classStudent)
                        .status(classEntity.getStartDate().after(new Date()) || classEntity.getEndDate().before(new Date()) ? TuitionStatus.UNPAID : TuitionStatus.PROCESSING)
                        .method(PaymentMethod.CASH)
                        .build())
                .toList();
        tuitionRepository.saveAll(tuitionRecords);
        classRepository.save(classEntity);
    }

    public void removeClassStudents(Long id) {
        classStudentRepository.deleteById(id);
    }
}
