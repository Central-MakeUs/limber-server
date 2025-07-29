package club.cmc.limber.oauth.infrastructure.token;

import club.cmc.limber.oauth.domain.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTokenJpaRepository extends JpaRepository<UserToken, Long> {
    Optional<UserToken> findByUserId(Long userId);
    Optional<UserToken> findByRefreshToken(String refreshToken);
    void deleteByUserId(Long userId);
}

