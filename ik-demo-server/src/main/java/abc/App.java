package abc;

import ch.qos.logback.core.util.FileUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationHome;

import java.io.File;
import java.io.IOException;

/**
 * TODO
 *
 * @author Qicz
 */
@SpringBootApplication
public class App {

    public static void main(String[] args) throws IOException {
        // 拷贝SpringBoot配置&ik配置
        ApplicationHome applicationHome = new ApplicationHome(App.class);
        File source = applicationHome.getSource();
        if (source != null) {
            String absolutePath = source.getAbsolutePath() + "/config";
            File file = new File(absolutePath);
            FileUtils.copyDirectory(file, new File("config"));
        }
        SpringApplication.run(App.class, args);
    }
}
