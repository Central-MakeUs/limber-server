package club.cmc.limber;

import club.cmc.limber.configuration.LimberApplicationBuilder;
import club.cmc.limber.oauth.OAuthModuleConfiguration;
import club.cmc.limber.user.UserModuleConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

//@SpringBootConfiguration
//@EnableAutoConfiguration
//@ComponentScan(basePackages = "club.cmc.limber")
//@ComponentScan(basePackages = "club.cmc.limber.integrate")
@SpringBootApplication(scanBasePackages = "club.cmc.limber") // 또는 정확히 oauth.api 등
public class LimberApplication {

    public static final String PROPS_CONFIG_NAME = "spring.config.name: application, user, oauth, global, common";

    public static void main(String[] args) {
        SpringApplication application = new LimberApplicationBuilder(LimberApplication.class)
                .properties(PROPS_CONFIG_NAME)
                .build(args);

        application.run(args);
    }

}