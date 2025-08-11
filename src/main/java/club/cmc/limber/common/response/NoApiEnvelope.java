package club.cmc.limber.common.response;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE}) // 메서드나 클래스에 붙일 수 있음
@Retention(RetentionPolicy.RUNTIME)            // 런타임까지 유지
@Documented
public @interface NoApiEnvelope {
}
