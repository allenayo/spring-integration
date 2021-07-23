package com.allenayo.sj.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {

    /**
     * 获取文件的格式
     *
     * @param fileName
     * @return
     */
    public static String getExt(String fileName) {
        if (fileName == null || fileName == "") return "";
        int idx = fileName.lastIndexOf(".");
        if (idx == -1) return "";
        return fileName.substring(idx + 1);
    }

    /**
     * 获取文件的格式
     *
     * @param file
     * @return
     */
    public static String getExt(File file) {
        if (file == null) return "";
        return getExt(file.getName());
    }

    public static void transferTo(InputStream is, String output) throws IOException {
        if (is == null) return;

        FileOutputStream fos = new FileOutputStream(output);
        byte[] buff = new byte[1024];
        int len;
        while ((len = is.read(buff)) != -1) {
            fos.write(buff, 0, len);
        }
        is.close();
        fos.close();
    }

}