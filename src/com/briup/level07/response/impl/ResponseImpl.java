package com.briup.level07.response.impl;

import com.briup.level07.response.Response;
import com.briup.level07.utils.MimeParseUtil;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class ResponseImpl implements Response {


    /**
     * 套接字
     */
    private Socket socket;

    /**
     * 请求路径
     */
    private String requestPath;

    /**
     * 文件
     */
    private File file;


    /**
     * 缓冲输出流
     */
    private BufferedOutputStream bufferedOutputStream;


    /**
     * 缓冲输入流
     */
    private BufferedInputStream bufferedInputStream;


    public ResponseImpl(Socket socket,String requestPath) throws IOException {
        this.socket = socket;
        this.requestPath = requestPath;
        buildResponseLine();
        //响应头（不写）
        buildResponseHeader();
        //构建响应空行
        buildResponseNullLine();

        //构建响应体
        buildResponseBody();
    }



    @Override
    public void buildResponseLine() throws IOException {
        /**
         * 让file指向具体的文件
         */
        if ("/".equals(this.requestPath)){
            //首页
            file = new File("D:/resource/index.html");
        }else{
            //不是首页
            file = new File("D:/resource"+this.requestPath);
        }

        //创建缓冲字节输出流
        bufferedOutputStream = new BufferedOutputStream(this.socket.getOutputStream());

        /**
         * 判断文件是否存在   发送响应状态行
         */
        if (file.exists()){
            //文件存在
            bufferedOutputStream.write("HTTP/1.1 200 OK \r\n".getBytes());
        }else{
            //文件不存在
            file = new File("D:/resource/error.html");
            bufferedOutputStream.write("HTTP/1.1 404 Not_Found \r\n".getBytes());
        }
    }

    @Override
    public void buildResponseHeader() throws IOException {
        /**
         *    1.txt
         *    1.txt.txt.txt.jpg
         *    abc
         */

        //拿到文件的后缀名
        String[] split = file.getName().split("[.]");
        String suffix = split[split.length - 1];

        if (Objects.isNull(MimeParseUtil.getMimeType(suffix))){
            //没查到
            bufferedOutputStream.write(("Content-Type: "+MimeParseUtil.getMimeType("*") + "\r\n").getBytes());
        }else{
            //查到了
            bufferedOutputStream.write(("Content-Type: "+MimeParseUtil.getMimeType(suffix) + "\r\n").getBytes());
        }
    }

    @Override
    public void buildResponseNullLine() throws IOException {
        //响应空行
        bufferedOutputStream.write("\r\n".getBytes());
    }

    @Override
    public void buildResponseBody() throws IOException {
        //创建缓冲字节输入流（读取文件）
        bufferedInputStream =new BufferedInputStream(new FileInputStream(file));
        //创建标识
        int flag;

        //创建字节数数组
        byte[] data = new byte[1024];

        //读取文件
        while ((flag = bufferedInputStream.read(data)) != -1){
            //响应体
            bufferedOutputStream.write(data,0,flag);
        }
    }

    @Override
    public BufferedOutputStream getBufferedOutputStream() {
        return bufferedOutputStream;
    }

    @Override
    public BufferedInputStream getBufferedInputStream() {
        return bufferedInputStream;
    }
}