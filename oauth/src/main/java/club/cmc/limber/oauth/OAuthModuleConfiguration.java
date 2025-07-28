package club.cmc.limber.oauth;

import club.cmc.limber.common.module.config.ModuleConfiguration;
import club.cmc.limber.common.module.info.ModuleNameInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class OAuthModuleConfiguration extends ModuleConfiguration {

    @Bean("oAuthModuleNameInfo")
    public ModuleNameInfo moduleNameInfo() {
        return new ModuleNameInfo("oauth-module"); // setter로 바인딩됨
    }

    @Override
    public ModuleNameInfo moduleInfoProperties() {
        return moduleNameInfo(); // 또는 필드에 @Autowired로 주입
    }

}