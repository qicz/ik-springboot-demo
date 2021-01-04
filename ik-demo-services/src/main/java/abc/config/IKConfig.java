package abc.config;

import abc.kit.Kit;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.wltea.analyzer.cfg.Configuration;

/**
 * IKConfig
 *
 * @author Qicz
 */
@org.springframework.context.annotation.Configuration
public class IKConfig {

    @Bean
    public Configuration ikConfiguration() {

        String path = System.getProperty("user.dir");
        // 仅在idea中实时调试需要，与config所在的目录必须一致，此处为ik-demo-config/resources
        if (!Kit.runningAsJar) {
            path += "/ik-demo-config/resources";
        }
        Environment environment = new Environment(Settings.builder().put("path.home", path).build(), null);
        Settings settings = Settings.builder()
                .put("use_smart", false)
                .put("enable_lowercase", false)
                .put("enable_remote_dict", false)
                .build();
        return new Configuration(environment, settings).setUseSmart(false);
    }
}
