package repository;

import model.Account;
import model.TuitionRecord;
import model.User;
import model.enums.TuitionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TuitionRepository extends JpaRepository<TuitionRecord, Long> {

    @Override
    @Query("""
            SELECT t FROM TuitionRecord t
            JOIN FETCH t.classStudent cs
            JOIN FETCH cs.classEntity c
            WHERE t.id = :id
            """)
    Optional<TuitionRecord> findById(Long id);

    @Query("""
            SELECT t FROM TuitionRecord t
            JOIN FETCH t.classStudent cs
            JOIN FETCH cs.classEntity c
            WHERE c.tuitionDueDate < :now
            AND t.status = :tuitionStatus
            """)
    List<TuitionRecord> findByTuitionDateBeforeAndTuitionStatusProcessing(Date now, TuitionStatus tuitionStatus);
}
