package repository;

import model.Homeroom;
import model.HomeroomStudent;
import model.Student;
import model.enums.HomeroomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HomeroomStudentRepository extends JpaRepository<HomeroomStudent, Long> {
    
    /**
     * Find all students in a homeroom with eager loading
     */
    @Query("SELECT hs FROM HomeroomStudent hs JOIN FETCH hs.student s JOIN FETCH s.user WHERE hs.homeroom.id = :homeroomId")
    List<HomeroomStudent> findByHomeroomIdWithStudent(@Param("homeroomId") Long homeroomId);
    
    /**
     * Find all homerooms for a student with eager loading
     */
    @Query("SELECT hs FROM HomeroomStudent hs JOIN FETCH hs.homeroom h JOIN FETCH h.teacher t JOIN FETCH t.user WHERE hs.student.id = :studentId")
    List<HomeroomStudent> findByStudentIdWithHomeroom(@Param("studentId") Long studentId);
    
    /**
     * Find a specific homeroom-student relationship
     */
    Optional<HomeroomStudent> findByHomeroomAndStudent(Homeroom homeroom, Student student);
    
    /**
     * Find all homeroom-student relationships by homeroom
     */
    List<HomeroomStudent> findByHomeroom(Homeroom homeroom);
    
    /**
     * Find all homeroom-student relationships by student
     */
    List<HomeroomStudent> findByStudent(Student student);
    
    /**
     * Find all homeroom-student relationships by homeroom and status
     */
    List<HomeroomStudent> findByHomeroomAndStatus(Homeroom homeroom, HomeroomStatus status);
    
    /**
     * Find all homeroom-student relationships by student and status
     */
    List<HomeroomStudent> findByStudentAndStatus(Student student, HomeroomStatus status);
    
    /**
     * Delete all homeroom-student relationships by homeroom
     */
    void deleteByHomeroom(Homeroom homeroom);
    
    /**
     * Delete homeroom-student relationship by homeroom and student
     */
    void deleteByHomeroomAndStudent(Homeroom homeroom, Student student);
    
    /**
     * Check if student is already in any homeroom with ANTICIPATED status
     */
    boolean existsByStudentAndStatus(Student student, HomeroomStatus status);
    
    /**
     * Count students in a homeroom
     */
    long countByHomeroom(Homeroom homeroom);
    
    /**
     * Count students in a homeroom with a specific status
     */
    long countByHomeroomAndStatus(Homeroom homeroom, HomeroomStatus status);
}