package club.cmc.limber.oauth.application;

import club.cmc.limber.oauth.domain.TokenPair;
import club.cmc.limber.oauth.infrastructure.token.JwtTokenProvider;
import club.cmc.limber.oauth.infrastructure.token.UserTokenRepository;
import club.cmc.limber.user.domain.entity.User;
import club.cmc.limber.user.domain.entity.UserRepository;
import com.nimbusds.oauth2.sdk.TokenResponse;
import com.nimbusds.oauth2.sdk.token.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserTokenRepository userTokenRepository;
    private final UserRepository userRepository;

    public TokenPair issueTokens(User user) {
        String accessToken = jwtTokenProvider.createAccessToken(user);
        String refreshToken = jwtTokenProvider.createRefreshToken(user);

        userTokenRepository.save(user.getId(), refreshToken, jwtTokenProvider.getRefreshExpiration());

        return TokenPair.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public TokenPair refreshAccessToken(String refreshToken) {
        if (!jwtTokenProvider.isValidToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.");
        }

        Long userId = Long.parseLong(jwtTokenProvider.extractUserId(refreshToken));

        // 저장된 토큰과 비교
        String savedToken = userTokenRepository.findByUserId(userId);
        if (savedToken == null || !savedToken.equals(refreshToken)) {
            throw new IllegalArgumentException("서버에 저장된 리프레시 토큰과 일치하지 않습니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        String newAccessToken = jwtTokenProvider.createAccessToken(user);

        return TokenPair.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken) // 그대로 반환
                .build();
    }

}
