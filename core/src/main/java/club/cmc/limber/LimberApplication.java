package club.cmc.limber;

import club.cmc.limber.configuration.LimberApplicationBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = "club.cmc.limber.integrate")
public class LimberApplication {

    public static final String PROPS_CONFIG_NAME = "spring.config.name: core, user, oauth";

    public static void main(String[] args) {
        SpringApplication application = new LimberApplicationBuilder(LimberApplication.class)
                .properties(PROPS_CONFIG_NAME)
                .build(args);

        application.run(args);
    }

}