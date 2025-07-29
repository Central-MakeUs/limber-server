package club.cmc.limber.oauth.infrastructure.token;

public interface UserTokenRepository {
    void save(Long userId, String token, long expiresInMillis);
    String findByUserId(Long userId);
    void delete(Long userId);
}
