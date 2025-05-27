package repository;

import model.AttendanceHistory;
import model.ClassStudent;
import model.enums.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AttendanceHistoryRepository extends JpaRepository<AttendanceHistory, Long> {

    @Query("""
            SELECT ah FROM AttendanceHistory ah
            JOIN FETCH ah.attendance a
            JOIN FETCH ah.classStudent cs
            WHERE cs = :classStudent
            """)
    List<AttendanceHistory> findByClassStudent(ClassStudent classStudent);

    @Query("""
            SELECT COUNT(ah) FROM AttendanceHistory ah
            WHERE ah.status IN (:attendanceStatus, :attendanceStatus1)
            AND ah.classStudent = :classStudent
            """)
    Integer findByTypeAndClassStudent(AttendanceStatus attendanceStatus, AttendanceStatus attendanceStatus1, ClassStudent classStudent);

    @Query("""
            SELECT COUNT(ah) FROM AttendanceHistory ah
            WHERE ah.classStudent = :classStudent
            AND ah.status IN (:status, :status1)
            """)
    Integer countByClassStudentAndStatusAndStatus(ClassStudent classStudent, AttendanceStatus status, AttendanceStatus status1);
}
