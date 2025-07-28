package club.cmc.limber.oauth.infrastructure.user;

import club.cmc.limber.oauth.application.TokenService;
import club.cmc.limber.oauth.domain.OAuthUserInfo;
import club.cmc.limber.oauth.domain.TokenPair;
import club.cmc.limber.user.domain.entity.User;
import club.cmc.limber.user.domain.entity.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserClientImpl implements UserClient {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    public UserClientImpl(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    @Override
    public AuthResponseDto findOrCreateUser(OAuthUserInfo oAuthUserInfo) {
        User user = userRepository
                .findByLoginTypeAndOauthId(String.valueOf(oAuthUserInfo.getProvider()), oAuthUserInfo.getProviderId())
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .loginType(String.valueOf(oAuthUserInfo.getProvider()))
                                .oauthId(oAuthUserInfo.getProviderId())
                                .email(oAuthUserInfo.getEmail())
                                .nickname(oAuthUserInfo.getNickname())
                                .build()
                ));

        TokenPair tokenPair = tokenService.issueTokens(user);

        return AuthResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .accessToken(tokenPair.accessToken())
                .refreshToken(tokenPair.refreshToken())
                .build();
    }
}
