package club.cmc.limber.configuration;

import club.cmc.limber.context.DbType;
import club.cmc.limber.properties.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(DataSourceProperties.class)
public class DataSourceConfig {

    private final DataSourceProperties props;

    public DataSourceConfig(DataSourceProperties props) {
        this.props = props;
    }

    @Bean
    @Primary
    public DataSource routingDataSource() {
        DataSource write = buildDataSource(props.getWrite());
        DataSource read = buildDataSource(props.getRead());

        Map<Object, Object> sources = new HashMap<>();
        sources.put(DbType.WRITE, write);
        sources.put(DbType.READ, read);

        RoutingDataSource routingDataSource = new RoutingDataSource();
        routingDataSource.setDefaultTargetDataSource(write);
        routingDataSource.setTargetDataSources(sources);
        return routingDataSource;
    }

    private DataSource buildDataSource(DataSourceProperties.DataSourceDetail detail) {
        return DataSourceBuilder.create()
                .url(detail.getUrl())
                .username(detail.getUsername())
                .password(detail.getPassword())
                .driverClassName(props.getDriverClassName())
                .build();
    }
}
