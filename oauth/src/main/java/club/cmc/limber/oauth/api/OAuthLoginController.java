package club.cmc.limber.oauth.api;

import club.cmc.limber.oauth.application.OAuthLoginService;
import club.cmc.limber.oauth.application.TokenService;
import club.cmc.limber.oauth.domain.OAuthUserInfo;
import club.cmc.limber.oauth.domain.TokenPair;
import club.cmc.limber.oauth.dto.TokenRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
public class OAuthLoginController {

    private final OAuthLoginService oAuthLoginService;

    public OAuthLoginController(OAuthLoginService oAuthLoginService) {
        this.oAuthLoginService = oAuthLoginService;
    }

    @GetMapping("/kakao")
    public ResponseEntity<OAuthUserInfo> loginWithKakao(@RequestParam String code) {
        OAuthUserInfo userInfo = oAuthLoginService.loginWithKakao(code);
        return ResponseEntity.ok(userInfo);
    }

}
