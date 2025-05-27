package repository;

import model.Class;
import model.ClassStudent;
import model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassStudentRepository extends JpaRepository<ClassStudent, Long> {

    @Query("""
            SELECT cs FROM ClassStudent cs
            JOIN FETCH cs.classEntity c
            JOIN FETCH c.teacher t
            JOIN FETCH t.user tu
            JOIN FETCH c.subject sub
            JOIN FETCH cs.student s
            WHERE s = :student
            AND ( MONTH(c.startDate) = :month OR MONTH(c.endDate) = :month )
            AND ( YEAR(c.startDate) = :year OR YEAR(c.endDate) = :year )
            """)
    List<ClassStudent> findByStudentAndMonthAndYear(Student student, Integer month, Integer year);

    @Query("""
            SELECT cs FROM ClassStudent cs
            JOIN FETCH cs.classEntity c
            JOIN FETCH cs.tuitionRecord t
            WHERE cs.student = :student
            """)
    Page<ClassStudent> findByStudent(Student student, Pageable pageable);

    @Query("""
            SELECT cs FROM ClassStudent cs
            JOIN FETCH cs.classEntity c
            JOIN FETCH cs.tuitionRecord t
            WHERE t.status= PROCESSING OR t.status = UNPAID
            AND cs.student = :student
            """)
    Page<ClassStudent> findByStudentAndNotPaid(Student student, Pageable pageable);

    @Override
    Optional<ClassStudent> findById(Long aLong);

    boolean existsByIdAndClassEntity(Long id, Class classEntity);

    @Query("""
            SELECT cs FROM ClassStudent cs
            JOIN FETCH cs.student s
            JOIN FETCH cs.tuitionRecord t
            JOIN FETCH s.user u
            JOIN FETCH cs.classEntity c
            WHERE c = :classEntity
            """)
    List<ClassStudent> findByClassEntity(Class classEntity);
}
