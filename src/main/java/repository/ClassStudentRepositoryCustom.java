package repository;

import model.ClassStudent;
import model.Student;
import model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassStudentRepositoryCustom extends JpaRepository<ClassStudent, Long> {
    
    /**
     * Find all class enrollments for a student
     */
    List<ClassStudent> findByStudent(Student student);
    
    /**
     * Find all class enrollments for a student in classes of a specific subject
     */
    @Query("SELECT cs FROM ClassStudent cs WHERE cs.student = :student AND cs.classEntity.subject = :subject")
    List<ClassStudent> findByStudentAndSubject(@Param("student") Student student, @Param("subject") Subject subject);
}