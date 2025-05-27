package service.student;

import dto.response.BaseResponse;
import dto.response.student.StudentGradeResponse;
import exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.ClassStudent;
import model.Student;
import model.StudentExamination;
import model.enums.ExaminationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import repository.ClassStudentRepository;
import repository.StudentExaminationRepository;
import repository.StudentRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentGradeServiceImpl {
    private final StudentRepository studentRepository;
    private final ClassStudentRepository classStudentRepository;
    private final StudentExaminationRepository studentExaminationRepository;
    public BaseResponse<List<StudentGradeResponse>> getGrades(int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Student student = studentRepository.findByUserAccountUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        Page<ClassStudent> classStudents = classStudentRepository.findByStudent(student, pageable);
        if (classStudents.isEmpty()) {
            return BaseResponse.ok(List.of(), "No grades found", new util.Paging(page, size, 0, 0));
        }

        List<StudentGradeResponse> studentGradeResponses = classStudents.getContent().stream()
                .map(classStudent ->{
                    StudentExamination midtermExam = studentExaminationRepository.findByClassStudentAndExamination_Type(classStudent, ExaminationType.MIDTERM);
                    StudentExamination finalExam = studentExaminationRepository.findByClassStudentAndExamination_Type(classStudent, ExaminationType.FINAL);
                    return StudentGradeResponse.builder()
                        .classId(classStudent.getClassEntity().getId())
                        .className(classStudent.getClassEntity().getClassName())
                        .finalTermWeight(classStudent.getClassEntity().getFinalTermWeight())
                        .midtermGrade(midtermExam != null ? midtermExam.getGrade() : null)
                        .finalGrade(finalExam != null ? finalExam.getGrade() : null)
                        .build();})
                .toList();
        return BaseResponse.ok(studentGradeResponses, "Grades retrieved successfully", new util.Paging(page, size, classStudents.getTotalElements(), classStudents.getTotalPages()));
    }
}
