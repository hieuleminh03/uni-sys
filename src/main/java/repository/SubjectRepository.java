package repository;

import model.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    
    /**
     * Find subject by code
     */
    Optional<Subject> findByCode(String code);
    
    /**
     * Find subject by name
     */
    Optional<Subject> findByName(String name);
    
    /**
     * Find all subjects with pagination
     */
    @Query("SELECT s FROM Subject s")
    Page<Subject> findAllSubjects(Pageable pageable);
    
    /**
     * Find subject by ID with eager loading of classes
     */
    @Query("SELECT s FROM Subject s LEFT JOIN FETCH s.classes WHERE s.id = :id")
    Optional<Subject> findByIdWithClasses(@Param("id") Long id);
}
