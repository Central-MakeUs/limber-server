package club.cmc.limber.domain.security.repository;

public interface UserTokenRepository {
    void save(Long userId, String token, long expiresInMillis);
    String findByUserId(Long userId);
    void delete(Long userId);
}
