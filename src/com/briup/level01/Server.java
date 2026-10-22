package com.briup.level01;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {

        //声明一台服务器
        ServerSocket serverSocket = null;
        //声明Socket对象
        Socket socket = null;
        //声明内存字节输出流
        ByteArrayOutputStream baos = null;
        try {
            //创建一台TCP服务器
            serverSocket = new ServerSocket(9999);

            System.out.println("TCP服务器创建成功，等待客户端的连接.....");

            //接收客户端的连接
            socket = serverSocket.accept();

            System.out.println("客户端连接成功，客户端IP："+socket.getLocalAddress());

            //创建内存输出流对象
            baos = new ByteArrayOutputStream();

            //创建标识，记录上一次读取到的位置
            int flag;

            //创建数组，存储每一次读取的内容
            byte[] data = new byte[1024];

            //读取客户端发送过来的请求
            while ((flag = socket.getInputStream().read(data)) != -1){
                //将读取到的数据先写出到内存
                baos.write(data,0,flag);
            }
            //输出读取到的内容
            System.out.println(baos.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            //关闭资源
            try {
                //关闭内存字节输出流
                if (baos != null){
                    baos.close();
                }
                //关闭套接字
                if (socket != null){
                    socket.close();
                }
                //关闭服务器对象
                if (serverSocket != null){
                    serverSocket.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}