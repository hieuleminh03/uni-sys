package repository;

import model.Announcement;
import model.enums.AnnouncementTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    
    List<Announcement> findByTarget(AnnouncementTarget target);
    
    @Query("SELECT a FROM Announcement a WHERE a.target = :target ORDER BY a.date DESC")
    List<Announcement> findByTargetOrderByDateDesc(@Param("target") AnnouncementTarget target);
    
    @Query("SELECT a FROM Announcement a ORDER BY a.date DESC")
    List<Announcement> findAllOrderByDateDesc();
    
    @Query("SELECT a FROM Announcement a")
    Page<Announcement> findAll(Pageable pageable);
    
    @Query(value = "SELECT DISTINCT a FROM Announcement a JOIN FETCH a.admin admin JOIN FETCH admin.user WHERE a.id IN :ids ORDER BY a.date DESC")
    List<Announcement> findAllWithAdminByIds(@Param("ids") List<Long> ids);
    
    // For students
    @Query("SELECT a FROM Announcement a WHERE a.target = 'STUDENT' ORDER BY a.date DESC")
    Page<Announcement> findAllForStudents(Pageable pageable);
    
    @Query("SELECT a FROM Announcement a WHERE a.id = :id AND a.target = 'STUDENT'")
    Optional<Announcement> findByIdForStudents(@Param("id") Long id);
    
    @Query("SELECT a FROM Announcement a WHERE a.target = 'STUDENT' " +
           "AND a.date BETWEEN :startDate AND :endDate ORDER BY a.date DESC")
    List<Announcement> findByWeekForStudents(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);
    
    // For teachers
    @Query("SELECT a FROM Announcement a WHERE a.target = 'TEACHER' ORDER BY a.date DESC")
    Page<Announcement> findAllForTeachers(Pageable pageable);
    
    @Query("SELECT a FROM Announcement a WHERE a.id = :id AND a.target = 'TEACHER'")
    Optional<Announcement> findByIdForTeachers(@Param("id") Long id);
    
    @Query("SELECT a FROM Announcement a WHERE a.target = 'TEACHER' " +
           "AND a.date BETWEEN :startDate AND :endDate ORDER BY a.date DESC")
    List<Announcement> findByWeekForTeachers(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);
    
    @Query("SELECT a FROM Announcement a JOIN FETCH a.admin admin JOIN FETCH admin.user WHERE a.id = :id")
    Optional<Announcement> findByIdWithAdmin(@Param("id") Long id);
}
