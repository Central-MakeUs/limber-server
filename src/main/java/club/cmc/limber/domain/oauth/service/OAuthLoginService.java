package club.cmc.limber.domain.oauth.service;

import club.cmc.limber.common.oauth.KakaoOAuthClient;
import club.cmc.limber.common.oauth.KakaoOAuthUserInfoResponse;
import club.cmc.limber.domain.oauth.dto.OAuthProvider;
import club.cmc.limber.domain.oauth.dto.OAuthUserInfo;
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
