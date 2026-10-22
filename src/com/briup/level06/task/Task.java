package com.briup.level06.task;

import com.briup.level06.request.Request;
import com.briup.level06.request.impl.RequestImpl;

import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.Set;

public class Task implements Runnable{

    private Socket socket;

    public Task(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        /*
            处理客户端发送到服务器的信息     请求
         */


        //声明缓冲字节输出流
        BufferedOutputStream bufferedOutputStream = null;

        //声明缓冲字节输入流
        BufferedInputStream bufferedInputStream = null;
        //声明请求对象
        Request request = null;

        try {
            //创建请求对象
            request = new RequestImpl(socket);

            /*
              处理响应
                响应状态行
                    协议版本号    响应状态码    响应信息
                响应头（消息报头）
                响应空行
                响应体（数据）
              */

            /**
             * 1. 判断客户端请求资源路径所对应的文件是否存在
             */

            //声明File对象
            File file = null;

            /**
             * 让file指向具体的文件
             */
            if ("/".equals(request.getPath())){
                //首页
                file = new File("D:/resource/index.html");
            }else{
                //不是首页
                file = new File("D:/resource"+request.getPath());
            }

            //创建缓冲字节输出流
            bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());


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

            //响应头（不写）

            //响应空行
            bufferedOutputStream.write("\r\n".getBytes());

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

                if (request.getBufferedReader() != null){
                    request.getBufferedReader().close();
                }

                if (socket != null){
                    socket.close();
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}