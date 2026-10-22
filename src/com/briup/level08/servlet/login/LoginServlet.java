package com.briup.level08.servlet.login;

import com.briup.level08.request.Request;
import com.briup.level08.response.Response;
import com.briup.level08.servlet.Servlet;

import java.io.IOException;

public class LoginServlet implements Servlet {
    @Override
    public void servletHandler(Response response, Request request) throws IOException {
        //响应
        //响应行
        response.getBufferedOutputStream().write("HTTP/1.1 200 OK \r\n".getBytes());
        //响应头
        response.getBufferedOutputStream().write("Content-Type: text/html \r\n".getBytes());
        //响应空行
        response.getBufferedOutputStream().write("\r\n".getBytes());
        //响应体
        response.getBufferedOutputStream().write("<!DOCTYPE html>\r\n".getBytes());
        response.getBufferedOutputStream().write("<html lang=\"en\"> \r\n".getBytes());
        response.getBufferedOutputStream().write("<head> \r\n".getBytes());
        response.getBufferedOutputStream().write("<meta charset=\"UTF-8\"> \r\n".getBytes());
        response.getBufferedOutputStream().write("<title>欢迎界面</title> \r\n".getBytes());
        response.getBufferedOutputStream().write("</head> \r\n".getBytes());
        response.getBufferedOutputStream().write("<body> \r\n".getBytes());
        response.getBufferedOutputStream().write("<marquee> \r\n".getBytes());
        response.getBufferedOutputStream().write(("<h1>欢迎您："+ request.getParameters().get("username") +"</h1> \r\n").getBytes());
        response.getBufferedOutputStream().write("</marquee> \r\n".getBytes());
        response.getBufferedOutputStream().write("<a href=\"http://localhost:9999\">首页</a> \r\n".getBytes());
        response.getBufferedOutputStream().write("</body> \r\n".getBytes());
        response.getBufferedOutputStream().write("</html> \r\n".getBytes());
    }
}