package abc;

import abc.kit.Kit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

/**
 * App
 *
 * @author Qicz
 */
@SpringBootApplication
public class App {

    public static void main(String[] args) throws IOException {
        Kit.copyConfigInJar(App.class);
        SpringApplication.run(App.class, args);
    }
}
