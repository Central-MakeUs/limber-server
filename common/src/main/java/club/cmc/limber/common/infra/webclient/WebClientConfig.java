package club.cmc.limber.common.infra.webclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import javax.net.ssl.SSLException;
import java.time.Duration;

@Slf4j
@Configuration
@EnableConfigurationProperties(WebClientProperties.class)
public class WebClientConfig {

    private final WebClientProperties properties;

    public WebClientConfig(WebClientProperties properties) {
        this.properties = properties;
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(makeHttpClient()))
                .exchangeStrategies(makeExchangeStrategies())
                .filters(customFilter -> {
                    customFilter.add(makeRequestCustomFilter());
                    customFilter.add(makeResponseCustomFilter());
                })
                .build();
    }

    private HttpClient makeHttpClient() {
        return HttpClient.create(makeConnectionProvider())
                .secure(sslContextSpec -> {
                    try {
                        sslContextSpec.sslContext(SslContextBuilder
                                .forClient()
                                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                                .build());
                    } catch (SSLException e) {
                        throw new RuntimeException(e);
                    }
                })
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, properties.getConnectionTimeoutMillisecond())
                .doOnConnected(connection ->
                        connection.addHandlerLast(new ReadTimeoutHandler(properties.getReadTimeoutSecond()))
                                .addHandlerLast(new WriteTimeoutHandler(properties.getWriteTimeoutSecond()))
                );
    }

    private ConnectionProvider makeConnectionProvider() {
        WebClientProperties.Connection.ConnectionProvider cp = properties.getConnection().getProvider();
        log.info(">>> ConnectionProvider name: {}", cp.getName());

        if (cp.getName() == null) {
            throw new IllegalStateException("webclient.connection.provider.name 설정이 null입니다");
        }

        return ConnectionProvider.builder(cp.getName())
                .maxConnections(cp.getMaxConnections())
                .pendingAcquireMaxCount(cp.getPendingAcquireMaxCount())
                .pendingAcquireTimeout(Duration.ofSeconds(cp.getPendingAcquireTimeoutSecond()))
                .build();
    }


    private ExchangeStrategies makeExchangeStrategies() {
        return ExchangeStrategies.builder()
                .codecs(configurer -> {
                    configurer.defaultCodecs().maxInMemorySize(properties.getMemory().getMaxInMemorySizeOfByte());
                    configurer.customCodecs().register(new Jackson2JsonDecoder(new ObjectMapper(), MediaType.TEXT_PLAIN));
                })
                .build();
    }

    private ExchangeFilterFunction makeRequestCustomFilter() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("[REQUEST] => URL : {} / method : {} / Headers : {}",
                    clientRequest.url(), clientRequest.method(), clientRequest.headers());
            return Mono.just(clientRequest);
        });
    }

    private ExchangeFilterFunction makeResponseCustomFilter() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            log.info("[RESPONSE] <= Headers : {}", clientResponse.headers().asHttpHeaders());
            return Mono.just(clientResponse);
        });
    }
}
