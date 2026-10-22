package com.briup.level02;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    public static void main(String[] args) {
        //声明一台TCP服务器
        ServerSocket serverSocket = null;
        //声明一个套接字对象
        Socket socket = null;
        //声明缓冲字符输入流
        BufferedReader bufferedReader = null;
        //声明一个缓冲字节输出流
        BufferedOutputStream bufferedOutputStream = null;
        //声明一个缓冲字节输入流
        BufferedInputStream bufferedInputStream = null;

        try {
            //创建一台TCP服务器
            serverSocket = new ServerSocket(9999);

            System.out.println("服务器启动成功，等待客户端的连接.....");
            //接受客户端的连接  拿到套接字对象
            socket = serverSocket.accept();

            System.out.println("客户端连接成功，客户端ip："+socket.getLocalAddress());

            //数据来源：客户端发送过来的请求
            //转换流   字节流  ---> 字符流   -----> 缓冲字符输入流
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //读取请求行
            String requestLine = bufferedReader.readLine();

            System.out.println(requestLine);

            //String 记录每一行数据
            String line;
            //读取剩余的内容
            while ((line = bufferedReader.readLine()) != null){
                if ("".equals(line)){
                    System.out.println();
                    break;
                }else{
                    System.out.println(line);
                }
            }

            //拿到资源路径信息
            String[] split = requestLine.split("[ ]");
            System.out.println("请求方式："+split[0]);
            System.out.println("请求资源路径："+split[1]);
            System.out.println("协议版本："+split[2]);

            //声明File对象
            File file = null;

            //创建文件时，可能遇到两种情况
            if ("/".equals(split[1])){
                //请求资源路径  /    index.html
                file = new File("D:/resource/index.html");
            }else{
                //请求资源路径不是  /   指向具体的资源
                file = new File("D:/resource"+split[1]);
            }

            //确定数据去向   客户端    socket
            bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());


            //判断文件是否存在
            if (file.exists()){
                //文件存在
                //返回响应行
                bufferedOutputStream.write("HTTP/1.1 200 OK \r\n".getBytes());
            }else {
                //文件不存在
                file = new File("D:/resource/error.html");
                //返回响应行
                bufferedOutputStream.write("HTTP/1.1 404 NotFound \r\n".getBytes());
            }

            //写回响应头（不写）

            //响应空行
            bufferedOutputStream.write("\r\n".getBytes());

            //响应体
            //读取文件  将文件读取到程序中   数据来源：文件
            bufferedInputStream = new BufferedInputStream(new FileInputStream(file));

            //定义标识
            int flag;

            //定义字节数组
            byte[] data = new byte[1024];

            //循环读取文件内容
            while ((flag = bufferedInputStream.read(data)) != -1){
                //确定数据去向  客户端
                bufferedOutputStream.write(data,0,flag);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            //关闭资源
            try {
                if (bufferedOutputStream != null){
                    bufferedOutputStream.close();
                }

                if (bufferedInputStream != null){
                    bufferedInputStream.close();
                }

                if (bufferedReader != null){
                    bufferedReader.close();
                }

                if (socket != null){
                    socket.close();
                }

                if (serverSocket != null){
                    serverSocket.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}