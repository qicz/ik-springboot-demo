package abc.kit;

import org.springframework.boot.system.ApplicationHome;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * Kit
 *
 * @author Qicz
 */
public final class Kit {

    public static boolean runningAsJar = false;

    /**
     * 拷贝SpringBoot配置&ik配置
     * @param appClass spring boot 启动类
     * @throws IOException io exception
     */
    public static void copyConfigInJar(Class<?> appClass) throws IOException {
        ApplicationHome applicationHome = new ApplicationHome(appClass);
        File source = applicationHome.getSource();
        if (source != null) {
            String absolutePath = source.getAbsolutePath();
            Kit.runningAsJar = absolutePath.endsWith("jar");
            if (!Kit.runningAsJar) {
                return;
            }

            final String configPath = "config/";
            File config = new File(System.getProperty("user.dir") + "/" + configPath);
            if (config.exists() || !config.mkdir()) {
                return;
            }

            JarFile jarFile = new JarFile(source);
            final Set<String> configFiles = new HashSet<String>(){{
                add(".properties");
                add(".yaml");
                add(".yml");
                add(".xml");
            }};
            for (Enumeration<? extends ZipEntry> entries = jarFile.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = entries.nextElement();
                String entryName = entry.getName();
                // 仅拷贝config目录或properties,yaml。
                boolean isConfig = false;
                int lastIndexOf = entryName.indexOf(configPath);
                String outPath = "";
                // has config dir
                if (lastIndexOf != -1) {
                    outPath = entryName.substring(lastIndexOf);
                    isConfig = true;
                } else {
                    lastIndexOf = entryName.lastIndexOf(".");
                    if (lastIndexOf != -1) {
                        String file = entryName.substring(entryName.lastIndexOf("/") + 1);
                        boolean pomFile = "pom.properties".equals(file) || "pom.xml".equals(file);
                        isConfig = configFiles.contains(entryName.substring(lastIndexOf)) && !pomFile;
                        if (isConfig) {
                            outPath = String.join("", configPath, file);
                        }
                    }
                }

                if (!isConfig) {
                    continue;
                }
                InputStream jarFileInputStream = jarFile.getInputStream(entry);
                File currentFile = new File(outPath.substring(0, outPath.lastIndexOf('/')));;
                if (!currentFile.exists() && !currentFile.mkdirs()) {
                    continue;
                }
                // 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                if (new File(outPath).isDirectory()) {
                    continue;
                }

                FileOutputStream out = new FileOutputStream(outPath);
                byte[] bytes = new byte[1024];
                int len;
                while ((len = jarFileInputStream.read(bytes)) > 0) {
                    out.write(bytes, 0, len);
                }
                jarFileInputStream.close();
                out.close();
            }
        }
    }
}
