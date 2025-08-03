package club.cmc.limber.common;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class DbChecker {

    private DataSource dataSource;

    public DbChecker(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void init() {
        try (Connection conn = dataSource.getConnection()) {
            System.out.println("✅ (PostConstruct) DB URL: " + conn.getMetaData().getURL());
            System.out.println("✅ (PostConstruct) DB USER: " + conn.getMetaData().getUserName());
        } catch (Exception e) {
            System.out.println("❌ DB 연결 실패: " + e.getMessage());
        }
    }
}
