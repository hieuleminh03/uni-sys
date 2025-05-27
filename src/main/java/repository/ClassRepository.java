package repository;

import model.Teacher;
import model.enums.DayOfWeek;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import model.Class;
import model.Student;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClassRepository extends JpaRepository<Class, Long> {

    @Override
    @Query("SELECT c FROM Class c JOIN FETCH c.teacher t JOIN FETCH t.user JOIN FETCH c.subject s JOIN FETCH c.schedules LEFT JOIN FETCH c.students")
    Page<Class> findAll(Pageable pageable);

    @Override
    @Query("SELECT c FROM Class c JOIN FETCH c.teacher t JOIN FETCH t.user JOIN FETCH c.subject s JOIN FETCH c.schedules LEFT JOIN FETCH c.students WHERE c.id = :id")
    Optional<Class> findById(Long id);

    @Query("""
            SELECT c FROM Class c
            JOIN FETCH c.schedules
            WHERE c.teacher=:teacher
            """)
    List<Class> findByTeacher(Teacher teacher);

    @Query("""
            SELECT c FROM Class c
            JOIN FETCH c.schedules s
            WHERE s.room = :room
            """)
    List<Class> findBySchedule(String room);

    @Query("""
                SELECT cs.classEntity
                FROM ClassStudent cs
                JOIN cs.classEntity c
                WHERE cs.student = :student
            """)
    List<Class> findByStudent(@Param("student") Student student);

    @Query("""
            SELECT COUNT(cs) FROM ClassStudent cs
            WHERE cs.classEntity.id = :classId
            """)
    Integer countByStudent(@Param("classId") Long classId);

    @Query("""
            SELECT c FROM Class c
            JOIN FETCH c.students cs
            JOIN FETCH cs.student s
            JOIN FETCH s.user
            WHERE c.id = :classId
            """)
    Optional<Class> findAttendenceById(Long classId);

    Page<Class> findClassByTeacher(Teacher teacher, Pageable pageable);


    @Query("""
            SELECT c FROM Class c
            JOIN FETCH c.teacher t
            JOIN FETCH c.subject s
            WHERE t = :teacher
            AND ( MONTH(c.startDate) = :month OR MONTH(c.endDate) = :month )
            AND ( YEAR(c.startDate) = :year OR YEAR(c.endDate) = :year )
            """)
    List<Class> findByAndTeacherMonthAndYear(Teacher teacher, Integer month, Integer year);

    @Query("""
            SELECT c FROM Class c
            JOIN FETCH c.examinations e
            """)
    Page<Class> findAllAddExam(Pageable pageable);

    @Query("""
            SELECT c FROM Class c
            JOIN FETCH c.students cs
            JOIN FETCH cs.student s
            WHERE s = :student
            """)
    Page<Class> findAllAddExamAndStudent(Pageable pageable, Student student);

    Page<Class> findByStudents_Student(Student student,Pageable pageable);
}
