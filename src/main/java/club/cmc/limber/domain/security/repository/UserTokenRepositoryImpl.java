package club.cmc.limber.domain.security.repository;

import club.cmc.limber.domain.security.dto.UserToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserTokenRepositoryImpl implements UserTokenRepository {

    private final UserTokenJpaRepository jpaRepository;

    @Override
    public void save(String userId, String token, long expiresInMillis) {
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
    public Optional<UserToken> findByUserId(String userId) {
        return jpaRepository.findByUserId(userId);
    }

    @Override
    public void delete(String userId) {
        jpaRepository.deleteByUserId(userId);
    }
}
