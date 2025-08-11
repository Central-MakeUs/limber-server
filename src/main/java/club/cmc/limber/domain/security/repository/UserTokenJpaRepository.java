package club.cmc.limber.domain.security.repository;

import club.cmc.limber.domain.security.dto.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTokenJpaRepository extends JpaRepository<UserToken, Long> {
    Optional<UserToken> findByUserId(String userId);
    Optional<UserToken> findByRefreshToken(String refreshToken);
    void deleteByUserId(String userId);
}

