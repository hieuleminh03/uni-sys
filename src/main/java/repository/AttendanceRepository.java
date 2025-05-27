package repository;

import model.Account;
import model.Attendance;
import model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

}
