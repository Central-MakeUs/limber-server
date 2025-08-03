package club.cmc.limber.common.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("LIMBER API 문서")
                        .description("LIMBER 모듈 기반 API 명세")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("LIMBER Backend Team")
                                .email("dev@cmc.club")
                        )
                );
    }
}
