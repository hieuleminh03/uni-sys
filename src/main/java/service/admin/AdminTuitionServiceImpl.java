package service.admin;

import dto.response.BaseResponse;
import dto.response.admin.TuitionListResponse;
import exception.ConflictTimeException;
import exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.Class;
import model.ClassStudent;
import model.Student;
import model.TuitionRecord;
import model.enums.TuitionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import repository.ClassStudentRepository;
import repository.StudentRepository;
import repository.TuitionRepository;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminTuitionServiceImpl {
    private final TuitionRepository tuitionRepository;
    private final StudentRepository studentRepository;
    private final ClassStudentRepository classStudentRepository;

    public BaseResponse<List<TuitionListResponse>> getTuitionListByStudent(Long studentId, int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
        Page<ClassStudent> classStudents = classStudentRepository.findByStudent(student,pageable);
        if (classStudents.isEmpty()) {
            return BaseResponse.ok(
                    List.of(),
                    "No tuititon found",
                    new util.Paging(page, size, 0, 0)
            );
        }
        return BaseResponse.ok(
                classStudents.getContent().stream()
                        .map(classStudent -> TuitionListResponse.builder()
                                .className(classStudent.getClassEntity().getClassName())
                                .id(classStudent.getTuitionRecord().getId())
                                .tuition(classStudent.getClassEntity().getTuition())
                                .tuitionStatus(classStudent.getTuitionRecord().getStatus())
                                .build())
                        .toList(),
                "Tuition list retrieved successfully",
                new util.Paging(page, size, classStudents.getTotalElements(), classStudents.getTotalPages())
        );
    }

    public BaseResponse<List<TuitionListResponse>> getNotPaidTuitionListByStudent(Long studentId, int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
        Page<ClassStudent> classStudents = classStudentRepository.findByStudentAndNotPaid(student,pageable);
        if (classStudents.isEmpty()) {
            return BaseResponse.ok(
                    List.of(),
                    "No tuititon found",
                    new util.Paging(page, size, 0, 0)
            );
        }
        return BaseResponse.ok(
                classStudents.getContent().stream()
                        .map(classStudent -> TuitionListResponse.builder()
                                .className(classStudent.getClassEntity().getClassName())
                                .id(classStudent.getTuitionRecord().getId())
                                .tuition(classStudent.getClassEntity().getTuition())
                                .tuitionStatus(classStudent.getTuitionRecord().getStatus())
                                .build())
                        .toList(),
                "Tuition list retrieved successfully",
                new util.Paging(page, size, classStudents.getTotalElements(), classStudents.getTotalPages())
        );
    }

    public BaseResponse<String> updateTuitionStatus(Long tuitionId , TuitionStatus status) {
        TuitionRecord tuitionRecord = tuitionRepository.findById(tuitionId)
                .orElseThrow(() -> new ResourceNotFoundException("Tuition record not found with id: " + tuitionId));

        tuitionRecord.setStatus(status);
        tuitionRepository.save(tuitionRecord);
        return BaseResponse.accepted("Tuition status updated successfully", "Tuition record paid successfully");
    }
}
