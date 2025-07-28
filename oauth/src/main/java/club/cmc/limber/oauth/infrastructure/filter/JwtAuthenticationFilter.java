package club.cmc.limber.oauth.infrastructure.filter;

import club.cmc.limber.oauth.infrastructure.config.AuthWhitelist;
import club.cmc.limber.oauth.infrastructure.token.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();

        if (AuthWhitelist.PUBLIC_URLS.stream().anyMatch(path::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (jwtTokenProvider.isValidToken(token)) {
                String userId = jwtTokenProvider.extractUserId(token);

                // 인증 객체 생성 (여기선 권한 없이 기본 인증 처리)
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userId, null, null);

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 시큐리티 컨텍스트에 등록
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                log.warn("Invalid or expired JWT token");
            }
        }

        // 필터 체인 계속 진행
        filterChain.doFilter(request, response);
    }
}
