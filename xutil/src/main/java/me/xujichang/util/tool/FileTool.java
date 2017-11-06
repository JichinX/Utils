package me.xujichang.util.tool;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 文件工具
 *
 * @author xjc
 *         Created by xjc on 2017/5/26.
 */

public class FileTool {
    /**
     * 解压文件
     *
     * @param zipFile
     * @param path
     */
    public static void unZipFile(final File zipFile, final String path) {
        LogTool.d("unZip.....dest:" + path);
        // 这里缓冲区我们使用4KB，
        int BUFFER = 4096;
        // 保存每个zip的条目名称
        String strEntry;
        try {
            // 缓冲输出流
            BufferedOutputStream dest = null;
            FileInputStream fis = new FileInputStream(zipFile);
            ZipInputStream zis = new ZipInputStream(
                    new BufferedInputStream(fis));
            // 每个zip条目的实例
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                try {
                    int count;
                    byte[] data = new byte[BUFFER];
                    strEntry = entry.getName();
                    File entryFile = new File(path + File.separator + strEntry);
                    File entryDir = new File(entryFile.getParent());
                    if (!entryDir.exists()) {
                        entryDir.mkdirs();
                    }
                    FileOutputStream fos = new FileOutputStream(entryFile);
                    dest = new BufferedOutputStream(fos, BUFFER);
                    while ((count = zis.read(data, 0, BUFFER)) != -1) {

                        dest.write(data, 0, count);
                    }
                    dest.flush();
                    dest.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            zis.close();
        } catch (Exception cwj) {
            cwj.printStackTrace();
        }
    }
}
