package repository;

import model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query("SELECT t FROM Token t WHERE t.user.id = :userId AND t.expired = false AND t.revoked = false")
    List<Token> findAllValidTokensByUser(Long userId);

    Optional<Token> findByToken(String token);
    
    @Transactional
    @Modifying
    @Query("UPDATE Token t SET t.revoked = true, t.expired = true WHERE t.token = :token")
    int revokeToken(String token);
}
