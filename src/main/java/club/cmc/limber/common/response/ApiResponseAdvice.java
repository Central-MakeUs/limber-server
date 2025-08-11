package club.cmc.limber.common.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class ApiResponseAdvice implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;

    public ApiResponseAdvice(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        // Swagger 등 제외 (원하면 더 추가)
        String uri = request.getRequestURI();
        if (uri.startsWith("/v3/api-docs") || uri.startsWith("/swagger-ui")) return false;

        // @NoApiEnvelope 적용된 핸들러는 제외
        return !returnType.hasMethodAnnotation(NoApiEnvelope.class);
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class selectedConverterType,
                                  ServerHttpRequest req,
                                  ServerHttpResponse res) {
        // 이미 래핑되어 있으면 그대로
        if (body instanceof CustomApiResponse<?> wrapped) return wrapped;

        // void/빈 응답
        if (body == null) return CustomApiResponse.success();

        // String은 특별 처리 (StringHttpMessageConverter 대응)
        if (body instanceof String s) {
            try {
                res.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                return objectMapper.writeValueAsString(CustomApiResponse.success(s));
            } catch (Exception e) {
                // 실패 시 그냥 원문 반환 (최악 회피)
                return s;
            }
        }

        // 그 외는 다 감싸기
        return CustomApiResponse.success(body);
    }
}
