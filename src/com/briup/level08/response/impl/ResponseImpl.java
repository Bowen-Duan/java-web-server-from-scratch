package com.briup.level08.response.impl;


import com.briup.level08.request.Request;
import com.briup.level08.response.Response;
import com.briup.level08.servlet.Servlet;
import com.briup.level08.system.SystemConstant;
import com.briup.level08.utils.LoadArgsUtils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

public class ResponseImpl implements Response {

    private Socket socket;

    private String requestPath;

    private BufferedInputStream bufferedInputStream;

    private BufferedOutputStream bufferedOutputStream;

    private File file;

    private String dynamicName;

    private Request request;


    public ResponseImpl(Socket socket,Request request) throws IOException {
        this.socket = socket;
        this.requestPath = request.getPath();
        this.request = request;
        this.bufferedOutputStream = new BufferedOutputStream(this.socket.getOutputStream());
    }


    /**
     * 响应处理程序(核心方法)
     */
    @Override
    public void responseHandler() throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        //创建文件对象
        createFile();
        //判断是否为动态资源
        if (isDynamic()){
            //动态资源
            // loginServlet.action   ----> loginServlet
            // 1.txt.jpg.action ------> 1.txt.jpg
            //文件名称
            String name = file.getName();
            //按.分割字符串
            String[] split = name.split("[.]");
            //遍历数组
            for (int i = 0; i < split.length - 1; i++) {
                if (i == 0){
                    dynamicName = split[i];
                }else{
                    dynamicName = dynamicName + "." + split[i];
                }
            }
            //反射创建对象  Class.forName(类的包名+类名)
            //获取字节码对象
            Class<?> aClass = Class.forName(LoadArgsUtils.getValue(dynamicName, SystemConstant.SERVLET_FLAG));
            //通过构造器创建对象
            Servlet servlet = (Servlet) aClass.getDeclaredConstructor().newInstance();
            //调用方法
            servlet.servletHandler(this,request);
        }else {
            //静态资源
            //确定返回文件
            returnFile();
            //构建响应行并发送
            bufferedOutputStream.write(buildResponseLine());
            //构建响应头并发送
            bufferedOutputStream.write(buildResponseHeader());
            //构建响应空行并发送
            bufferedOutputStream.write(buildResponseNullLine());
            //发送响应体
            sendResponseBody();
        }
    }


    /**
     * 是否为动态资源
     *
     * @return boolean
     */
    private boolean isDynamic(){
        //文件不存在
        if (file.exists()){
            //是静态资源
            return false;
        }
        //文件后缀名 .action结尾
        if (!"action".equals(returnFileSuffixName(file.getName()))){
            //静态资源
            return false;
        }
        //动态资源
        return true;
    }


    /**
     * 创建文件
     */
    private void createFile(){
        //1. 是否请求资源路径 ------> index.html
        if (LoadArgsUtils.getValue(SystemConstant.DEFAULT_PATH_KEY,SystemConstant.CONFIG_FLAG).equals(requestPath)){
            //    D:/resource/index.html
            file = new File(LoadArgsUtils.getValue(SystemConstant.ROOT_PATH_KEY,SystemConstant.CONFIG_FLAG)+LoadArgsUtils.getValue(SystemConstant.INDEX_FILE_KEY,SystemConstant.CONFIG_FLAG));
        }else {
            //    D:/resource/1.txt   D:/resource/hello.txt
            file = new File(LoadArgsUtils.getValue(SystemConstant.ROOT_PATH_KEY,SystemConstant.CONFIG_FLAG)+requestPath);
        }
    }

    /**
     * 确定返回文件
     *
     * @return {@link File }
     */
    private File returnFile(){
        // 2. 文件不存在，file------->error.html
        if (!file.exists()){
            file = new File(LoadArgsUtils.getValue(SystemConstant.ROOT_PATH_KEY,SystemConstant.CONFIG_FLAG)+LoadArgsUtils.getValue(SystemConstant.ERROR_FILE_KEY,SystemConstant.CONFIG_FLAG));
        }
        //返回文件
        return file;
    }


    /**
     * 返回文件后缀名称
     *
     * @return {@link String }
     */
    private String returnFileSuffixName(String fileName){
        /**
         * 1.txt
         * 1.txt.doc.xsl.jpg
         * abc
         */
        //分割字符串
        String[] data = fileName.split("[.]");
        //取到分割后字符串的最后一个元素
        return data[data.length - 1];
    }


    /**
     * 构建响应行
     *
     * @return {@link byte[] }
     */
    @Override
    public byte[] buildResponseLine() {
        if (file.exists()){
            //返回  HTTP/1.1 200 OK \r\n
            return (SystemConstant.HTTP_VERSION + SystemConstant.SPACES + SystemConstant.SUCCESS_CODE + SystemConstant.SPACES + LoadArgsUtils.getValue(SystemConstant.SUCCESS_CODE, SystemConstant.STATUS_CODE_FLAG) + SystemConstant.SPACES + SystemConstant.NEW_LINE).getBytes();
        }else{
            //返回  HTTP/1.1 404 Not_Found \r\n
            return (SystemConstant.HTTP_VERSION + SystemConstant.SPACES + SystemConstant.ERROR_CODE + SystemConstant.SPACES + LoadArgsUtils.getValue(SystemConstant.ERROR_CODE, SystemConstant.STATUS_CODE_FLAG) + SystemConstant.SPACES + SystemConstant.NEW_LINE).getBytes();
        }
    }

    /**
     * 生成响应头
     *
     * @return {@link byte[] }
     */
    @Override
    public byte[] buildResponseHeader() {
        //根据后缀名查找响应的Content-Type
        String value = LoadArgsUtils.getValue(returnFileSuffixName(file.getName()), SystemConstant.MIME_FLAG);
        //返回  Content-Type: value \r\n
        //return ("Content-Type: "+ value +"\r\n").getBytes();
        return (SystemConstant.CONTENT_TYPE + value + SystemConstant.NEW_LINE).getBytes();
    }

    /**
     * 构建响应空行
     *
     * @return {@link byte[] }
     */
    @Override
    public byte[] buildResponseNullLine() {
        return SystemConstant.NEW_LINE.getBytes();
    }

    @Override
    public void sendResponseBody() throws IOException {
        //初始化缓冲字节输入流
        bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
        //声明标识
        int flag;
        //定义字节数组，存储读取到的数据
        byte[] data = new byte[2048];
        //读取文件
        while ((flag = bufferedInputStream.read(data)) != -1){
            //写回响应
            bufferedOutputStream.write(data,0,flag);
        }
    }

    @Override
    public BufferedInputStream getBufferedInputStream() {
        return bufferedInputStream;
    }

    @Override
    public BufferedOutputStream getBufferedOutputStream() {
        return bufferedOutputStream;
    }
}