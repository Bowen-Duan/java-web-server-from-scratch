package com.briup.level05.utils;

import java.io.FileReader;
import java.io.IOException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {

    private static Properties prop = null;

    static {
        prop = new Properties();
        try {
            prop.load(new FileReader("E:\\code\\SXAU_Web_Server\\config\\mime.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String get(String key){
        return prop.getProperty(key);
    }

}

