package abc.services;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.cfg.Configuration;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringJoiner;

/**
 * TODO
 *
 * @author Qicz
 */
@Component
public class ABC {

    static String text = "IK Analyzer是一个结合词典分词和文法分词的中文分词开源工具包。它使用了全新的正向迭代最细粒度切分算法。";

    public ABC() throws IOException {
        this.ddoo();
    }

    public void ddoo() throws IOException {
        String path = Paths.get(System.getProperty("user.dir")).resolve("ik-demo-config").toString();
        System.out.println("====path========="+path);

        Settings settings =  Settings.builder()
                .put("use_smart", false)
                .put("enable_lowercase", false)
                .put("enable_remote_dict", false)
                .build();
        Environment environment = new Environment(Settings.builder().put("path.home", path).build(), null);
        Configuration configuration = new Configuration(environment,settings).setUseSmart(false);

        IKSegmenter segmenter = new IKSegmenter(new StringReader(text), configuration);
        Lexeme next;
        System.out.print("非智能分词结果：");
        StringJoiner stringJoiner = new StringJoiner(",");
        while((next=segmenter.next())!=null){
            String lexemeText = next.getLexemeText();
            stringJoiner.add(lexemeText);
        }
        System.out.println(stringJoiner);
        System.out.println();
        System.out.println("----------------------------分割线------------------------------");
        settings =  Settings.builder()
                .put("use_smart", true)
                .put("enable_lowercase", false)
                .put("enable_remote_dict", false)
                .build();
        configuration = new Configuration(environment,settings).setUseSmart(true);
        IKSegmenter smartSegmenter = new IKSegmenter(new StringReader(text), configuration);
        System.out.print("智能分词结果：");
        stringJoiner = new StringJoiner(",");
        while((next=smartSegmenter.next())!=null) {
            String lexemeText = next.getLexemeText();
            stringJoiner.add(lexemeText);
        }
        System.out.println(stringJoiner);
    }
}
