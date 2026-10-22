package com.briup.level08.response;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public interface Response {


    /**
     * 响应处理程序(核心方法)
     */
    public void responseHandler() throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;


    /**
     * 构建响应行
     *
     * @return {@link byte[] }
     */
    public byte[] buildResponseLine();


    /**
     * 生成响应头
     *
     * @return {@link byte[] }
     */
    public byte[] buildResponseHeader();


    /**
     * 构建响应空行
     *
     * @return {@link byte[] }
     */
    public byte[] buildResponseNullLine();


    /**
     * 发送响应正文
     */
    public void sendResponseBody() throws IOException;

    /**
     * 获取缓冲字节输入流
     *
     * @return {@link BufferedInputStream }
     */
    public BufferedInputStream getBufferedInputStream();

    /**
     * 获取缓冲输出流
     *
     * @return {@link BufferedOutputStream }
     */
    public BufferedOutputStream getBufferedOutputStream();
}