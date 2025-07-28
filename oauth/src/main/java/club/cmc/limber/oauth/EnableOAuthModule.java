package club.cmc.limber.oauth;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ComponentScan(basePackageClasses = OAuthModuleConfiguration.class)
@Import(OAuthModuleConfiguration.class)
public @interface EnableOAuthModule {
}
