package club.cmc.limber.oauth.infrastructure.user;

import club.cmc.limber.oauth.domain.OAuthUserInfo;

public interface UserClient {
    AuthResponseDto findOrCreateUser(OAuthUserInfo oAuthUserInfo);
}
