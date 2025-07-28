package club.cmc.limber.oauth.application;

import club.cmc.limber.oauth.domain.OAuthProvider;
import club.cmc.limber.oauth.domain.OAuthUserInfo;
import club.cmc.limber.oauth.infrastructure.oauth.KakaoOAuthClient;
import club.cmc.limber.oauth.infrastructure.oauth.KakaoOAuthUserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {

    private final KakaoOAuthClient kakaoOAuthClient;

    public OAuthUserInfo loginWithKakao(String code) {
        String accessToken = kakaoOAuthClient.getAccessToken(code);
        KakaoOAuthUserInfoResponse response = kakaoOAuthClient.getUserInfo(accessToken);

        return new OAuthUserInfo(
                OAuthProvider.KAKAO,
                String.valueOf(response.getId()),
                response.getEmail(),
                response.getNickname()
        );
    }
}
