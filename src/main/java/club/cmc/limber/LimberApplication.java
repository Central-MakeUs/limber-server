package club.cmc.limber;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LimberApplication {

	public static void main(String[] args) {
		SpringApplication.run(LimberApplication.class, args);
	}

}
