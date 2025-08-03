package club.cmc.limber.domain.security.api;

import club.cmc.limber.domain.security.dto.TokenPair;
import club.cmc.limber.domain.security.dto.TokenRequest;
import club.cmc.limber.domain.security.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class TokenController {

    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenPair> refresh(@RequestBody TokenRequest request) {
        TokenPair tokenPair = tokenService.refreshAccessToken(request.refreshToken());
        return ResponseEntity.ok(tokenPair);
    }

}
