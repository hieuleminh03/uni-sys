package repository;

import model.Account;
import model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttendanceHistoryRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUsername(String username);

    String user(User user);
}
