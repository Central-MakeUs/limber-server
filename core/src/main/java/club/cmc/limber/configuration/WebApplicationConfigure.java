package club.cmc.limber.configuration;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Slf4j
@Configuration
public class WebApplicationConfigure {

    @PostConstruct
    public void init() {
        log.info("[WebApplicationConfigure] Loaded");
    }
}
