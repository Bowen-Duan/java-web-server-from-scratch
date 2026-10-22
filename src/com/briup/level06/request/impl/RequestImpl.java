package com.briup.level06.request.impl;

import com.briup.level06.request.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class RequestImpl implements Request {

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求资源路径
     */
    private String path;


    /**
     * HTTP 版本
     */
    private String httpVersion;


    /**
     * 请求头
     */
    private Map<String,String> headers;


    /**
     * 请求参数
     */
    private Map<String,String> parameters;


    /**
     * 缓冲字符输入流
     */
    private BufferedReader bufferedReader;


    private Socket socket;


    public RequestImpl(Socket socket) {
        this.socket = socket;
        this.headers = new HashMap();
        this.parameters = new HashMap<>();


        try {
            //创建流对象   //字节流  --》 字符流   -----》 缓冲字符输入流
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //解析请求行
            parseRequestLine();
            //解析请求头
            parseRequestHeard();
            //解析请求体
            parseRequestBody();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void parseRequestLine() throws IOException {
        //读取请求行信息
        String requestLine = bufferedReader.readLine();

        //分割字符串
        String[] split = requestLine.split("[ ]");

        //为请求方式赋值
        this.method = split[0];
        //为请求资源路径赋值
        if (split[1].contains("?")){
            //有请求参数
            String[] pathAndParameters = split[1].split("[?]");
            this.path = pathAndParameters[0];

            //pathAndParameters[1]    username=briup&password=briup
            String[] keyEqValue = pathAndParameters[1].split("[&]");

            //遍历数组
            for (String kev : keyEqValue){
                //key=value
                String[] keyAndValue = kev.split("[=]");
                if (keyAndValue.length == 2){
                    parameters.put(keyAndValue[0],keyAndValue[1]);
                }
            }
        }else {
            //没有请求参数
            this.path = split[1];
        }
        //为协议版本号赋值
        this.httpVersion = split[2];
    }


    private void parseRequestHeard() throws IOException {
        //记录读到的字符串
        String line;

        while (!"".equals(line = bufferedReader.readLine())){
            //key: value
            String[] keyAndValue = line.split(": ");
            //添加判断
            if (keyAndValue.length == 2){
                headers.put(keyAndValue[0],keyAndValue[1]);
            }
        }
    }


    private void parseRequestBody() throws IOException {
        if (bufferedReader.ready()){
            //创建标识
            int flag;

            //创建字符数组
            char[] chars = new char[1024];

            flag = bufferedReader.read(chars);

            //转换成字符串
            String args = new String(chars, 0, flag);

            //username=briup&password=briup

            String[] keyEqValue = args.split("[&]");

            //遍历数组
            for (String kev : keyEqValue){
                //key=value
                String[] keyAndValue = kev.split("[=]");
                if (keyAndValue.length == 2){
                    parameters.put(keyAndValue[0],keyAndValue[1]);
                }
            }
        }
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getHttpVersion() {
        return httpVersion;
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public Map<String, String> getParameters() {
        return parameters;
    }

    @Override
    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }
}