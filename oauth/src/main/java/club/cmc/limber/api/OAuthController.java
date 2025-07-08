package club.cmc.limber.api;

import club.cmc.limber.application.OAuthLoginService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
public class OAuthController {

    private final OAuthLoginService oAuthLoginService;

    public OAuthController(OAuthLoginService oAuthLoginService) {
        this.oAuthLoginService = oAuthLoginService;
    }

}
