package com.briup.level07.utils;

import com.briup.level07.system.SystemConstant;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class MimeParseUtil {

    private static Properties properties;

    static {
        //初始化Properties对象
        properties = new Properties();
        try {
            //加载配置文件
            properties.load(new FileReader(SystemConstant.MIME_PATH));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 获取 MIME 类型
     *
     * @param fileSuffixName 文件后缀名称
     * @return {@link String }
     */
    public static String getMimeType(String fileSuffixName) {
        return properties.getProperty(fileSuffixName);
    }
}