package club.cmc.limber.domain.user.repository;


import club.cmc.limber.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일로 사용자 조회
    Optional<User> findByEmail(String email);

    // 소셜 로그인 정보로 사용자 조회
    Optional<User> findByLoginTypeAndOauthId(String loginType, String oauthId);

    // 닉네임으로 사용자 조회
    Optional<User> findByNickname(String nickname);
}