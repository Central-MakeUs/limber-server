package club.cmc.limber;

import club.cmc.limber.common.module.config.ModuleConfiguration;
import club.cmc.limber.common.module.info.ModuleNameInfo;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.beans.BeanProperty;

@Configuration
@EnableConfigurationProperties
public class UserModuleConfiguration extends ModuleConfiguration {

    @Override
    @BeanProperty
    @ConfigurationProperties("user.user-module")
    public ModuleNameInfo moduleInfoProperties() {
        return new ModuleNameInfo();
    }

}