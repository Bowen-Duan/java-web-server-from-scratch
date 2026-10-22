package com.briup.level07.response;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

public interface Response {

    /**
     * 构建响应行
     */
    public void buildResponseLine() throws IOException;


    /**
     * 构建生成响应头
     */
    public void buildResponseHeader() throws IOException;

    /**
     * 构建响应空行
     */
    public void buildResponseNullLine() throws IOException;


    /**
     * 构建响应正文
     */
    public void buildResponseBody() throws IOException;


    public BufferedOutputStream getBufferedOutputStream();


    public BufferedInputStream getBufferedInputStream();
}