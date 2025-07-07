package club.cmc.limber.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class KakaoOAuthClient {

    private final KakaoOAuthProperties properties;

    public KakaoOAuthClient(KakaoOAuthProperties properties) {
        this.properties = properties;
    }

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://kauth.kakao.com")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .build();

    public String getAccessToken(String code) {
        KakaoTokenResponse response = webClient.post()
                .uri("/oauth/token")
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", properties.getClientId())
                        .with("redirect_uri", properties.getRedirectUri())
                        .with("code", code))
                .retrieve()
                .bodyToMono(KakaoTokenResponse.class)
                .block();

        return response.getAccessToken();
    }

    public KakaoOAuthUserInfoResponse getUserInfo(String accessToken) {
        WebClient kakaoApiClient = WebClient.builder()
                .baseUrl("https://kapi.kakao.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .build();

        return kakaoApiClient.get()
                .uri("/v2/user/me")
                .retrieve()
                .bodyToMono(KakaoOAuthUserInfoResponse.class)
                .block();
    }

}
