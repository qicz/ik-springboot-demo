package abc.config;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.springframework.context.annotation.Bean;
import org.wltea.analyzer.cfg.Configuration;

/**
 * TODO
 *
 * @author Qicz
 */
@org.springframework.context.annotation.Configuration
public class IKConfig {

    @Bean
    public Configuration ikConfiguration() {
        String path = System.getProperty("user.dir");

        // 仅在idea中实时调试需要，与config所在的目录必须一致，此处为ik-demo-config
        if (path.endsWith("ik-demo-parent")) {
            path += "/ik-demo-config";
        }
        System.out.println("====path========="+path);

        Environment environment = new Environment(Settings.builder().put("path.home", path).build(), null);
        Settings settings = Settings.builder()
                .put("use_smart", false)
                .put("enable_lowercase", false)
                .put("enable_remote_dict", false)
                .build();
        return new Configuration(environment, settings).setUseSmart(false);
    }
}
