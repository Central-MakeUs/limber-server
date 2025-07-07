package club.cmc.limber.infrastructure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "oauth.kakao")
@Getter
@Setter
public class KakaoOAuthProperties {
    private String clientId;
    private String redirectUri;
}

