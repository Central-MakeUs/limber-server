package club.cmc.limber.common.infra.webclient;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("webclient")
@Getter
@Setter
public class WebClientProperties {

    private int connectionTimeoutMillisecond;
    private int readTimeoutSecond;
    private int writeTimeoutSecond;
    private Block block = new Block();
    private Connection connection = new Connection();
    private Memory memory = new Memory();

    @Getter
    @Setter
    public static class Connection {
        private ConnectionProvider provider = new ConnectionProvider();

        @Getter
        @Setter
        public static class ConnectionProvider {
            private String name;
            private int maxConnections;
            private int pendingAcquireMaxCount;
            private int pendingAcquireTimeoutSecond;
        }
    }

    @Getter
    @Setter
    public static class Memory {
        private int maxInMemorySizeOfByte;
    }

    @Getter
    @Setter
    public static class Block {
        private int timeoutOfMillis;
    }
}
