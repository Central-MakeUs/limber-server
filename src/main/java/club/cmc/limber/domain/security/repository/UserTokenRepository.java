package club.cmc.limber.domain.security.repository;

import club.cmc.limber.domain.security.dto.UserToken;

import java.util.Optional;

public interface UserTokenRepository {
    void save(String userId, String token, long expiresInMillis);
    Optional<UserToken> findByUserId(String userId);
    void delete(String userId);
}
