package repository;

import dto.response.admin.SearchStudentResponse;
import dto.response.admin.SearchTeacherResponse;
import model.Account;
import model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByAccount(Account account);

    boolean existsByEmail(String email);

    @Query("""
               SELECT new dto.response.admin.SearchTeacherResponse(
                   u.fullName,
                   u.avatarUrl,
                   u.email,
                   t.id
               )
                FROM User u
                JOIN Teacher t ON u.id = t.user.id
                WHERE u.fullName LIKE %:query% OR u.email LIKE %:query%
            """)
    List<SearchTeacherResponse> searchTeacherByFullNameAndEmail(String query);

    @Query("""
               SELECT new dto.response.admin.SearchStudentResponse(
                   u.fullName,
                   u.avatarUrl,
                   u.email,
                   s.id
               )
                FROM User u
                JOIN Student s ON u.id = s.user.id
                WHERE u.fullName LIKE %:query% OR u.email LIKE %:query%
            """)
    List<SearchStudentResponse> searchStudentByFullNameAndEmail(String query);
}
