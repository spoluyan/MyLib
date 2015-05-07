package pw.spn.mylib;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MylibApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MylibApplication.class);
        app.setShowBanner(false);
        app.run(args);
    }
}
