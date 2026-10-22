package com.briup.level08.utils;

import com.briup.level08.system.SystemConstant;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;


 //加载 参数 工具类

public class LoadArgsUtils {

    private static Properties configProperties;

    private static Properties mimeProperties;

    private static Properties statusCodeProperties;

    private static Properties servletProperties;


    static {
        //初始化Properties对象
        configProperties = new Properties();
        mimeProperties = new Properties();
        statusCodeProperties = new Properties();
        servletProperties = new Properties();

        try {
            //加载对应的配置文件
            configProperties.load(new FileReader(SystemConstant.CONFIG_PATH));
            mimeProperties.load(new FileReader(SystemConstant.MIME_PATH));
            statusCodeProperties.load(new FileReader(SystemConstant.STATUS_CODE_PATH));
            servletProperties.load(new FileReader(SystemConstant.SERVLET_PATH));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 获取值
     *  flag = 1   configProperties
     *  flag = 2   mimeProperties
     *  flag = 3   statusCodeProperties
     *
     * @param key  钥匙
     * @param flag 标志
     * @return {@link String }
     */
    public static String getValue(String key,int flag){
        if (flag == SystemConstant.CONFIG_FLAG){
            return configProperties.getProperty(key);
        } else if (flag == SystemConstant.MIME_FLAG) {
            return mimeProperties.getProperty(key);
        }else if (flag == SystemConstant.STATUS_CODE_FLAG){
            return statusCodeProperties.getProperty(key);
        }else {
            return servletProperties.getProperty(key);
        }
    }
}