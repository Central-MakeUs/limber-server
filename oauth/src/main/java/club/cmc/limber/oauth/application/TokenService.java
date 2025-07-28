package club.cmc.limber.oauth.application;

import club.cmc.limber.oauth.domain.TokenPair;
import club.cmc.limber.oauth.infrastructure.token.JwtTokenProvider;
import club.cmc.limber.oauth.infrastructure.token.RefreshTokenJpaRepository;
import club.cmc.limber.oauth.infrastructure.token.RefreshTokenRepository;
import club.cmc.limber.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenPair issueTokens(User user) {
        String accessToken = jwtTokenProvider.createAccessToken(user);
        String refreshToken = jwtTokenProvider.createRefreshToken(user);

        refreshTokenRepository.save(user.getId(), refreshToken, jwtTokenProvider.getRefreshExpiration());

        return TokenPair.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
