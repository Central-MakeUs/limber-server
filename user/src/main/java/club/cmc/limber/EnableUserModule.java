package club.cmc.limber;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ComponentScan(basePackageClasses = UserModuleConfiguration.class)
@Import(UserModuleConfiguration.class)
public @interface EnableUserModule {
}
