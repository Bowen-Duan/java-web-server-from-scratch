package com.briup.level08.request.impl;

import com.briup.level08.request.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class RequestImpl implements Request {

    private String method;

    private String path;

    private String httpVersion;

    private Map<String,String> headers;

    private Map<String,String> parameters;

    private BufferedReader bufferedReader;

    private Socket socket;


    public RequestImpl(Socket socket) throws IOException {
        this.socket = socket;
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.headers = new HashMap<>();
        this.parameters = new HashMap<>();

        //解析请求
        requestHandler();
    }

    /**
     * 请求处理程序（核心）
     */
    public void requestHandler() throws IOException {
        //解析请求行
        parseRequestLine();
        //解析请求头
        parseRequestHeaders();
        //解析请求体
        parseRequestBody();
    }


    /**
     * 解析请求行
     */
    public void parseRequestLine() throws IOException {
        //读取请求行信息   GET /1.txt?username=briup&password=briup HTTP/1.1
        String requestLine = bufferedReader.readLine();
        //按照空格进行分割字符串
        String[] data = requestLine.split("[ ]");

        this.method = data[0];

        if (data[1].contains("?")){
            //有参数
            //按照？分割字符串
            String[] pathAndParameters = data[1].split("[?]");
            ///1.txt
            this.path = pathAndParameters[0];
            //username=briup&password=briup
            String[] keyEqValue = pathAndParameters[1].split("[&]");
            for (String kev : keyEqValue) {
                //username=briup
                String[] keyAndValue = kev.split("[=]");
                this.parameters.put(keyAndValue[0],keyAndValue[1]);
            }
        }else{
            //没有参数
            this.path = data[1];
        }
        this.httpVersion = data[2];
    }


    /**
     * 解析请求头
     */
    private void parseRequestHeaders() throws IOException {
        //记录读到的一整行数据
        String line;
        //循环读取
        while (!"".equals(line = bufferedReader.readLine())){
            //key: value
            String[] keyAndValue = line.split(": ");
            if (keyAndValue.length == 2){
                this.headers.put(keyAndValue[0],keyAndValue[1]);
            }
        }
    }

    /**
     * 解析请求正文
     */
    private void parseRequestBody() throws IOException {
        if (bufferedReader.ready()){
            //有请求体
            //定义标识
            int flag;

            //定义一个字符数组
            char[] chars = new char[1024];

            //读取
            flag = bufferedReader.read(chars);

            //转换成字符串   username=briup&password=briup
            String parameter = new String(chars, 0, flag);

            String[] keyEqValue = parameter.split("[&]");

            for (String kev : keyEqValue) {
                //username=briup
                String[] keyAndValue = kev.split("[=]");
                //添加至参数集合
                this.parameters.put(keyAndValue[0],keyAndValue[1]);
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