package repository;

import model.Homeroom;
import model.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HomeroomRepository extends JpaRepository<Homeroom, Long> {
    
    /**
     * Find all homerooms with eager loading of teacher and user data
     */
    @Query("SELECT h FROM Homeroom h JOIN FETCH h.teacher t JOIN FETCH t.user")
    List<Homeroom> findAllWithTeacher();
    
    /**
     * Find homerooms by IDs with eager loading of teacher and user data
     */
    @Query("SELECT h FROM Homeroom h JOIN FETCH h.teacher t JOIN FETCH t.user WHERE h.id IN :ids")
    List<Homeroom> findAllWithTeacherByIds(@Param("ids") List<Long> ids);
    
    /**
     * Find a homeroom by ID with eager loading of teacher and user data
     */
    @Query("SELECT h FROM Homeroom h JOIN FETCH h.teacher t JOIN FETCH t.user WHERE h.id = :id")
    Optional<Homeroom> findByIdWithTeacher(@Param("id") Long id);
    
    /**
     * Find a homeroom by ID with eager loading of teacher, user, and student data
     */
    @Query("SELECT DISTINCT h FROM Homeroom h " +
           "JOIN FETCH h.teacher t JOIN FETCH t.user " +
           "LEFT JOIN FETCH h.students hs LEFT JOIN FETCH hs.student s LEFT JOIN FETCH s.user " +
           "WHERE h.id = :id")
    Optional<Homeroom> findByIdWithTeacherAndStudents(@Param("id") Long id);
    
    /**
     * Find all homerooms for a specific teacher
     */
    List<Homeroom> findByTeacher(Teacher teacher);
    
    /**
     * Find homeroom by name
     */
    Optional<Homeroom> findByName(String name);
    
    /**
     * Find all homerooms with pagination
     */
    @Query("SELECT h FROM Homeroom h")
    Page<Homeroom> findAll(Pageable pageable);

    boolean existsHomeroomByTeacher(Teacher teacher);
}
