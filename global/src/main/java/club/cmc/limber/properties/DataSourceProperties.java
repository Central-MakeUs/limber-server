package club.cmc.limber.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceProperties {

    private String driverClassName;

    private DataSourceDetail write;
    private DataSourceDetail read;

    @Getter
    @Setter
    public static class DataSourceDetail {
        private String url;
        private String username;
        private String password;
    }
}


