package abc.services;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.cfg.Configuration;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.util.StringJoiner;

/**
 * TODO
 *
 * @author Qicz
 */
@Component
public class ABC {

    static String text = "123abc;；.。。。\\l a吃 IK Analyzer是一个结合词典分词和文法分词的中文分词开源工具包。它使用了全新的正向迭代最细粒度切分算法。";

    private final Configuration configuration;

    public ABC(Configuration configuration) throws IOException {
        this.configuration = configuration;
        this.ddoo();
    }

    public void ddoo() throws IOException {
        IKSegmenter segmenter = new IKSegmenter(new StringReader(text), configuration);
        Lexeme next;
        System.out.print("非智能分词结果(ik_max_world)：");
        StringJoiner stringJoiner = new StringJoiner(",");
        while((next=segmenter.next())!=null){
            String lexemeText = next.getLexemeText();
            stringJoiner.add(lexemeText);
        }
        System.out.println(stringJoiner);
        System.out.println();
        System.out.println("----------------------------分割线------------------------------");

        this.configuration.setUseSmart(true);
        IKSegmenter smartSegmenter = new IKSegmenter(new StringReader(text), configuration);
        System.out.print("智能分词结果(ik_smart)：");
        stringJoiner = new StringJoiner(",");
        while((next=smartSegmenter.next())!=null) {
            String lexemeText = next.getLexemeText();
            stringJoiner.add(lexemeText);
        }
        System.out.println(stringJoiner);
    }
}
