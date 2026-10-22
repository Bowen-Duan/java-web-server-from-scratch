package com.briup.level06.request;

import java.io.BufferedReader;
import java.util.Map;

public interface Request {

    /**
     * 获取请求方式
     *
     * @return {@link String }
     */
    public String getMethod();


    /**
     * 获取请求资源路径
     *
     * @return {@link String }
     */
    public String getPath();


    /**
     * 获取 HTTP 版本
     *
     * @return {@link String }
     */
    public String getHttpVersion();


    /**
     * 获取请求头
     *
     * @return {@link Map }<{@link String },{@link String }>
     */
    public Map<String,String> getHeaders();


    /**
     * 获取请求参数
     *
     * @return {@link Map }<{@link String },{@link String }>
     */
    public Map<String,String> getParameters();


    /**
     * 获取缓冲字符输入流
     *
     * @return {@link BufferedReader }
     */
    public BufferedReader getBufferedReader();
}