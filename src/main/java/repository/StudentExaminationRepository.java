package repository;

import model.Account;
import model.ClassStudent;
import model.StudentExamination;
import model.User;
import model.enums.ExaminationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentExaminationRepository extends JpaRepository<StudentExamination, Long> {

    StudentExamination findByClassStudentAndExamination_Type(ClassStudent classStudent, ExaminationType type);
}
