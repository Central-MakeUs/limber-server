package club.cmc.limber.oauth.infrastructure.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponseDto {
    private Long id;
    private String email;
    private String nickname;
    private String accessToken;
    private String refreshToken;
}
