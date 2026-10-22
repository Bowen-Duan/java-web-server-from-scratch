package com.briup.level03;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        //声明服务器对象
        ServerSocket serverSocket = null;
        try {
            //搭建一台TCP服务器
            serverSocket = new ServerSocket(9999);
            System.out.println("服务器启动成功，等待客户端连接....");
            //死循环  要一直接受客户端的连接
            while (true){
                //接受客户端连接
                Socket socket = serverSocket.accept();
                System.out.println("客户端连接成功，客户端IP为："+socket.getLocalAddress());

                //单独为每一个客户端分配一个线程  并启动
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        /*
                            读取客户端请求
                         */
                        //声明缓冲字符输入流
                        BufferedReader bufferedReader = null;
                        //声明缓冲字节输出流
                        BufferedOutputStream bufferedOutputStream = null;
                        //声明缓冲字节输入流
                        BufferedInputStream bufferedInputStream = null;
                        try {
                            //创建缓冲字符输入流
                            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                            //获取请求行信息
                            String requestLine = bufferedReader.readLine();

                            //解析请求行信息
                            String[] split = requestLine.split("[ ]");

                            System.out.println("请求方式："+split[0]);
                            System.out.println("请求资源路径："+split[1]);
                            System.out.println("HTTP协议版本："+split[2]);

                            //声明File对象
                            File file = null;

                            if ("/".equals(split[1])){
                                //首页   index.html   存在
                                file = new File("D:/resource/index.html");
                            }else {
                                //根据资源路径生成的文件对象（存在  or 不存在）
                                file = new File("D:/resource"+split[1]);
                            }

                            //创建缓冲字节输出流
                            bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());

                            if (file.exists()){
                                //文件存在
                                // 响应行
                                bufferedOutputStream.write("HTTP/1.1 200 OK \r\n".getBytes());
                            }else {
                                //文件不存在
                                file = new File("D:/resource/error.html");
                                //响应行
                                bufferedOutputStream.write("HTTP/1.1 404 NOT_FOUND \r\n".getBytes());
                            }

                            //响应头(不管)

                            //响应空行
                            bufferedOutputStream.write("\r\n".getBytes());

                            //创建缓冲字节输入流（读取文件）
                            bufferedInputStream = new BufferedInputStream(new FileInputStream(file));

                            //标识
                            int flag;

                            //存储读取到的数据
                            byte[] data = new byte[1024];

                            //读取文件内容
                            while ((flag = bufferedInputStream.read(data)) != -1){
                                //响应体
                                bufferedOutputStream.write(data, 0, flag);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }finally {
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
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            if (serverSocket != null){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}