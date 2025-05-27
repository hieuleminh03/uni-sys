package service.student;

import dto.response.BaseResponse;
import dto.response.admin.TuitionListResponse;
import exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.ClassStudent;
import model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import repository.ClassStudentRepository;
import repository.StudentRepository;
import repository.TuitionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentTuitionServiceImpl {
    private final StudentRepository studentRepository;
    private final TuitionRepository tuitionRepository;
    private final ClassStudentRepository classStudentRepository;
    public BaseResponse<List<TuitionListResponse>> getAllTuition(int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Student student = studentRepository.findByUserAccountUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        Page<ClassStudent> classStudents = classStudentRepository.findByStudent(student, pageable);
        if (classStudents.isEmpty()) {
            return BaseResponse.ok(
                    List.of(),
                    "No tuition found",
                    new util.Paging(page, size, 0, 0)
            );
        }
        return BaseResponse.ok(
                classStudents.getContent().stream()
                        .map(classStudent -> TuitionListResponse.builder()
                                .className(classStudent.getClassEntity().getClassName())
                                .tuition(classStudent.getClassEntity().getTuition())
                                .tuitionStatus(classStudent.getTuitionRecord().getStatus())
                                .build())
                        .toList(),
                "Tuition list retrieved successfully",
                new util.Paging(page, size, classStudents.getTotalElements(), classStudents.getTotalPages())
        );
    }
}
