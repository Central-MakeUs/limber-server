package club.cmc.limber;

import club.cmc.limber.common.module.config.ModuleConfiguration;
import club.cmc.limber.common.module.info.ModuleNameInfo;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OAuthModuleConfiguration extends ModuleConfiguration {

    @Bean("oAuthModuleNameInfo")
    @ConfigurationProperties(prefix = "limber.oauth-module")
    public ModuleNameInfo moduleNameInfo() {
        return new ModuleNameInfo("oauth-module"); // setter로 바인딩됨
    }

    @Override
    public ModuleNameInfo moduleInfoProperties() {
        return moduleNameInfo(); // 또는 필드에 @Autowired로 주입
    }

}