package club.cmc.limber.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.List;

public class LimberApplicationBuilder {

    private SpringApplicationBuilder springBuilder;

    public LimberApplicationBuilder(Class<?> mainClass) {
        springBuilder = new SpringApplicationBuilder().sources(mainClass);
    }

    public LimberApplicationBuilder properties(String properties) {
        springBuilder.properties(properties);
        return this;
    }

    public SpringApplication build(String... args) {
        registerModules();
        return springBuilder.build(args);
    }

    public void registerModules() {
        // 모듈들을 하나의 Context로 등록
        springBuilder = springBuilder
                .sources(WebApplicationConfigure.class)
                .sources(getModuleConfiguration().toArray(new Class<?>[0]))
                .web(WebApplicationType.SERVLET);
    }

    private static List<Class<?>> getModuleConfiguration() {
        return List.of(
                club.cmc.limber.oauth.OAuthModuleConfiguration.class,
                club.cmc.limber.user.UserModuleConfiguration.class
        );
    }
}
