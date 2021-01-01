# ik springboot demo

> 主要测试验证ik的字典引入方式及分词



### 重点及注意事项

- ik-demo-server（启动类所在module）

  - 使用maven copy

    - ik-demo-config的目录结构

      ```
      ik-demo-config/resources/application.yml
      ik-demo-config/config/analysis-ik
      ```

      > resources仅放入SpringBoot的配置文件，config放入ik配置文件，这样可以减少SpringBoot打包到时候将ik的配置文件（有点大）一起打包到jar中。

    - pom.xml 配置

      ```xml
      <build>
              <plugins>
                  <plugin>
                      <groupId>org.springframework.boot</groupId>
                      <artifactId>spring-boot-maven-plugin</artifactId>
                      <configuration>
                          <mainClass>abc.App</mainClass>
                      </configuration>
                      <executions>
                          <execution>
                              <goals>
                                  <goal>repackage</goal>
                              </goals>
                          </execution>
                      </executions>
                  </plugin>
                  <plugin> <!-- 拷贝ik配置到${project.build.directory}/config下 -->
                      <artifactId>maven-resources-plugin</artifactId>
                      <version>2.6</version>
                      <executions>
                          <execution>
                              <id>copy-resources</id> <!-- here the phase you need -->
                              <phase>validate</phase>
                              <goals>
                                  <goal>copy-resources</goal>
                              </goals>
                              <configuration> <!--copyTo的目录-->
                                  <outputDirectory>${project.build.directory}/config</outputDirectory>
                                  <resources>
                                      <resource> <!--被copy的目录-->
                                          <directory>../ik-demo-config/config</directory>
                                          <filtering>true</filtering>
                                      </resource>
                                  </resources>
                              </configuration>
                          </execution>
                      </executions>
                  </plugin>
              </plugins>
              <resources> <!-- 指定SpringBoot资源配置位置 -->
                  <resource>
                      <directory>../ik-demo-config/resources</directory>
                  </resource>
                  <resource>
                      <directory>src/main/resources</directory>
                  </resource>
              </resources>
          </build>
      ```

  - 使用Java代码拷贝

    - ik-demo-config的目录结构

      ```
      ik-demo-config/config/analysis-ik...application.yml
      ```

      > resources直接放入config目录，config目录包括SpringBoot的配置文件及ik配置文件。这样便于应用启动时，从jar中拷贝config到jar同级目录。依赖org.apache.commons.io。

    - pom.xml 配置

      ```xml
       <build>
              <plugins>
                  <plugin>
                      <groupId>org.springframework.boot</groupId>
                      <artifactId>spring-boot-maven-plugin</artifactId>
                      <configuration>
                          <mainClass>abc.App</mainClass>
                      </configuration>
                      <executions>
                          <execution>
                              <goals>
                                  <goal>repackage</goal>
                              </goals>
                          </execution>
                      </executions>
                  </plugin>
              </plugins>
              <resources>
                  <resource>
                      <directory>../ik-demo-config/resources</directory>
                  </resource>
                  <resource>
                      <directory>src/main/resources</directory>
                  </resource>
              </resources>
          </build>
      ```

- idea中实时调试

  ```java
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
  ```

  

### demo

```java
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

        this.configuration.setUseSmart(true); // 切换配置
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
```

