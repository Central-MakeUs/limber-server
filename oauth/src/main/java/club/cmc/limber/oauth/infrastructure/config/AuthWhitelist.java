package club.cmc.limber.oauth.infrastructure.config;

import java.util.List;

public class AuthWhitelist {
    public static final List<String> PUBLIC_URLS = List.of(
            "/v1/auth/login",
            "/v1/auth/register",
            "/v1/public",
            "/swagger-ui",
            "/v3/api-docs",
            "/error",
            "/favicon.ico"
    );
}
