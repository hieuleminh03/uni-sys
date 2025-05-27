package repository;

import jakarta.validation.constraints.NotBlank;
import model.Account;
import model.ClassSchedule;
import model.Teacher;
import model.User;
import model.enums.DayOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface ClassScheduleRepository extends JpaRepository<ClassSchedule, Long> {
}
