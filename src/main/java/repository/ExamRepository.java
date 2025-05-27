package repository;

import model.Account;
import model.Class;
import model.Examination;
import model.User;
import model.enums.ExaminationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamRepository extends JpaRepository<Examination, Long> {

    @Query("""
            SELECT e FROM Examination e
            JOIN FETCH e.classEntity c
            JOIN FETCH e.studentExaminations se
            JOIN FETCH se.classStudent cs
            JOIN FETCH cs.student s
            JOIN FETCH s.user u
            WHERE c = :classEntity AND e.type = :type
            """)
    Examination findByClassEntityAndType(Class classEntity, ExaminationType type);

    List<Examination> findByClassEntity(Class classEntity);

    boolean existsByClassEntity(Class classEntity);
}
