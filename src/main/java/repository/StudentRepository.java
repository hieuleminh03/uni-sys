package repository;

import model.Account;
import model.Student;
import model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUser(User user);
    
    /**
     * Find student by ID and eagerly fetch the user relationship
     * to avoid LazyInitializationException
     */
    @Query("SELECT s FROM Student s JOIN FETCH s.user WHERE s.id = :id")
    Optional<Student> findByIdWithUser(@Param("id") Long id);
}
