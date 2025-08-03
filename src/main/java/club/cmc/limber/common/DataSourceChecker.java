package club.cmc.limber.common;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class DataSourceChecker implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🌐 [DB 접속 정보 확인]");
        System.out.println("▶ " + dataSource.getConnection().getMetaData().getURL());
        System.out.println("▶ 사용자: " + dataSource.getConnection().getMetaData().getUserName());
    }

}
