package club.cmc.limber.common.config.oauth;

import java.util.List;

public class AuthWhitelist {
    public static final List<String> PUBLIC_URLS = List.of(
            "/api/auth/login",
            "/api/auth/register",
            "/api/public",
            "/*/swagger-ui/*",
            "/swagger-ui/index.html",
            "/v3/api-docs",
            "/error",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**",
            "/favicon.ico"

    );
}
