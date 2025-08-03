package club.cmc.limber.domain.security.repository;

import club.cmc.limber.domain.security.dto.UserToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.ZonedDateTime;

@Repository
@RequiredArgsConstructor
public class UserTokenRepositoryImpl implements UserTokenRepository {

    private final UserTokenJpaRepository jpaRepository;

    @Override
    public void save(Long userId, String token, long expiresInMillis) {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime expiresAt = now.plus(Duration.ofMillis(expiresInMillis));

        UserToken entity = jpaRepository.findByUserId(userId)
                .map(existing -> {
                    existing.setRefreshToken(token);
                    existing.setExpiresAt(expiresAt);
                    existing.setUpdatedAt(now);
                    existing.setUpdatedBy("system");
                    return existing;
                })
                .orElseGet(() -> UserToken.builder()
                        .userId(userId)
                        .refreshToken(token)
                        .expiresAt(expiresAt)
                        .createdAt(now)
                        .build());

        jpaRepository.save(entity);
    }

    @Override
    public String findByUserId(Long userId) {
        return jpaRepository.findByUserId(userId)
                .map(UserToken::getRefreshToken)
                .orElse(null);
    }

    @Override
    public void delete(Long userId) {
        jpaRepository.deleteByUserId(userId);
    }
}
