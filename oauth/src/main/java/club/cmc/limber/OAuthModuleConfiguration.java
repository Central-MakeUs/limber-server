package club.cmc.limber;

import club.cmc.limber.common.module.config.ModuleConfiguration;
import club.cmc.limber.common.module.info.ModuleNameInfo;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.beans.BeanProperty;

@Slf4j
@Configuration
@EnableConfigurationProperties
public class OAuthModuleConfiguration extends ModuleConfiguration {

    @Override
    @BeanProperty
    @ConfigurationProperties(prefix = "limber.oauth-module")
    public ModuleNameInfo moduleInfoProperties() {
        return new ModuleNameInfo();
    }

    @PostConstruct
    public void debugProperties() {
        log.info("moduleNameInfo.name = {}", moduleInfoProperties().toString());
    }

}