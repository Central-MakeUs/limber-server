package club.cmc.limber;

import club.cmc.limber.common.module.config.ModuleConfiguration;
import club.cmc.limber.common.module.info.ModuleNameInfo;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserModuleConfiguration extends ModuleConfiguration {

    @Bean("userModuleNameInfo")
    @ConfigurationProperties(prefix = "limber.user-module")
    public ModuleNameInfo moduleNameInfo() {
        return new ModuleNameInfo("user-module"); // setter로 바인딩됨
    }

    @Override
    public ModuleNameInfo moduleInfoProperties() {
        return moduleNameInfo(); // 또는 필드에 @Autowired로 주입
    }

}