package club.cmc.limber.domain;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OAuthUserInfo {
    private OAuthProvider provider;
    private String providerId;
    private String email;
    private String nickname;
}
