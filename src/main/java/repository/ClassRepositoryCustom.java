package repository;

import model.Class;
import model.Subject;
import model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRepositoryCustom extends JpaRepository<Class, Long> {
    
    /**
     * Find all classes taught by a specific teacher
     */
    List<Class> findByTeacher(Teacher teacher);
    
    /**
     * Find all classes for a specific subject taught by a specific teacher
     */
    @Query("SELECT c FROM Class c WHERE c.teacher = :teacher AND c.subject = :subject")
    List<Class> findByTeacherAndSubject(@Param("teacher") Teacher teacher, @Param("subject") Subject subject);
}