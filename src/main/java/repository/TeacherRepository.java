package repository;

import model.Account;
import model.Teacher;
import model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByUser(User user);
    
    /**
     * Find teacher by ID and eagerly fetch the user relationship
     * to avoid LazyInitializationException
     */
    @Query("SELECT t FROM Teacher t JOIN FETCH t.user WHERE t.id = :id")
    Optional<Teacher> findByIdWithUser(@Param("id") Long id);
    
    /**
     * Find teacher by username and eagerly fetch the user relationship
     * to avoid LazyInitializationException
     */
    @Query("SELECT t FROM Teacher t JOIN FETCH t.user u JOIN FETCH u.account a WHERE a.username = :username")
    Optional<Teacher> findByUserAccountUsername(@Param("username") String username);
}
