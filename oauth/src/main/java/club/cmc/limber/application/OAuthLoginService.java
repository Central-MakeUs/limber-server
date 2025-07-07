package club.cmc.limber.application;

import club.cmc.limber.domain.OAuthUserInfo;
import club.cmc.limber.infrastructure.KakaoOAuthClient;
import club.cmc.limber.infrastructure.KakaoOAuthUserInfoResponse;
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
                "kakao",
                String.valueOf(response.getId()),
                response.getEmail(),
                response.getNickname()
        );
    }
}
