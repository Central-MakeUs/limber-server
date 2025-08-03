package club.cmc.limber.common.config.oauth;

import java.util.List;

public class AuthWhitelist {
    public static final List<String> PUBLIC_URLS = List.of(
            "/api/auth/login",
            "/api/auth/register",
            "/api/public",
            "*/swagger-ui/*",
            "/v3/api-docs",
            "/error",
            "/favicon.ico"
    );
}
