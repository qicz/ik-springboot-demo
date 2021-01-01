package abc.kit;

import abc.App;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.system.ApplicationHome;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * Kit
 *
 * @author Qicz
 */
public final class Kit {

    /**
     * 拷贝SpringBoot配置&ik配置
     * @param appClass spring boot 启动类
     * @throws IOException io exception
     */
    public static void copyConfig(Class<?> appClass) throws IOException {
        ApplicationHome applicationHome = new ApplicationHome(App.class);
        File source = applicationHome.getSource();
        if (source != null) {
            String absolutePath = source.getAbsolutePath();
            boolean aJar = absolutePath.endsWith("jar");
            if (aJar) {
                File config = new File(System.getProperty("user.dir") + "/config");
                if (!config.exists()) {
                    if (!config.mkdir()) {
                        return;
                    }
                    JarFile jarFile = new JarFile(source);
                    final String configPath = "config/";
                    for (Enumeration<? extends ZipEntry> entries = jarFile.entries(); entries.hasMoreElements(); ) {
                        ZipEntry entry = entries.nextElement();
                        String entryName = entry.getName();
                        // 仅拷贝config目录
                        if (!entryName.contains(configPath)) {
                            continue;
                        }
                        InputStream jarFileInputStream = jarFile.getInputStream(entry);
                        String outPath = (entryName.substring(entryName.indexOf(configPath)));

                        // 判断路径是否存在,不存在则创建文件路径
                        File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                        if (!file.exists() && !file.mkdirs()) {
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
            } else {
                absolutePath += "/config";
                File file = new File(absolutePath);
                FileUtils.copyDirectory(file, new File("config"));
            }
        }
    }
}
