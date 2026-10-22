package com.briup.level04;

import java.io.*;
import java.net.Socket;

public class Task implements Runnable{

    private Socket socket;

    public Task(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        //接受请求
        //声明一个缓冲字符输入流(读取请求)
        BufferedReader bufferedReader = null;

        //声明缓冲字节输出流（返回响应）
        BufferedOutputStream bufferedOutputStream = null;

        //声明缓冲字节输入流（读取资源文件）
        BufferedInputStream bufferedInputStream = null;

        try {
            //创建缓冲字符输入流
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //读取请求行信息
            String requestLine = bufferedReader.readLine();

            String[] split = requestLine.split("[ ]");

            System.out.println("请求方式： " + split[0]);
            System.out.println("请求资源路径： " + split[1]);
            System.out.println("协议版本号： " + split[2]);


            //返回响应
            /**
             * 确定返回的文件
             */
            //声明文件对象
            File file = null;

            if ("/".equals(split[1])){
                //文件指向首页
                file = new File("D:/resource/index.html");
            }else{
                //文件指向指定资源
                file = new File("D:/resource"+split[1]);
            }
            //创建缓冲字节输出流
            bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());

            //文件是否存在
            if (file.exists()){
                //存在
                bufferedOutputStream.write("HTTP/1.1 200 OK \r\n".getBytes());
            }else{
                //文件不存在
                file = new File("D:/resource/error.html");
                bufferedOutputStream.write("HTTP/1.1 404 Not_Found \r\n".getBytes());
            }

            //响应头（不管）

            //响应空行
            bufferedOutputStream.write("\r\n".getBytes());

            //创建缓冲字节输入流（读取资源文件）
            bufferedInputStream = new BufferedInputStream(new FileInputStream(file));

            //声明一个标识
            int flag;

            //创建一个字节数组   存储每一次读取到的数据
            byte[] data = new byte[1024];

            while ((flag = bufferedInputStream.read(data)) != -1){
                //响应体
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
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}