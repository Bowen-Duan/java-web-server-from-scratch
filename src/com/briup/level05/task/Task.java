package com.briup.level05.task;

import com.briup.level05.system.SystemConstant;
import com.briup.level05.utils.PropertiesUtil;

import java.io.*;
import java.net.Socket;

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

        //声明缓冲字符输入流
        BufferedReader bufferedReader = null;

        //声明缓冲字节输出流
        BufferedOutputStream bufferedOutputStream = null;

        //声明缓冲字节输入流
        BufferedInputStream bufferedInputStream = null;

        try {
            //创建流对象   //字节流  --》 字符流   -----》 缓冲字符输入流
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //读取请求行信息
            String requestLine = bufferedReader.readLine();

            System.out.println(requestLine);

            //分割字符串  拿到资源路径信息
            String[] split = requestLine.split("[ ]");

            System.out.println("请求方式："+split[0]);
            System.out.println("请求资源路径："+split[1]);
            System.out.println("HTTP协议版本："+split[2]);

            //读取请求头

            String line;

            while ((line = bufferedReader.readLine()) != null){
                if ("".equals(line)){
                    System.out.println();
                    break;
                }else {
                    System.out.println(line);
                }
            }

            //请求体
            if (bufferedReader.ready()){
                //创建标识
                int flag;

                //创建字符数组
                char[] chars = new char[1024];

                flag = bufferedReader.read(chars);

                System.out.println("====================");
                System.out.println(new String(chars,0,flag));
                System.out.println("====================");

            }



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
            if ("/".equals(split[1])){
                //首页
                file = new File(SystemConstant.HOME_PATH);
            }else{
                //不是首页
                file = new File("D:/resource"+split[1]);
            }

            //创建缓冲字节输出流
            bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());


            /**
             * 判断文件是否存在   发送响应状态行
             */
            if (file.exists()){
                //文件存在
                bufferedOutputStream.write(PropertiesUtil.get("success_response_head").getBytes());
            }else{
                //文件不存在
                file = new File("D:/resource/error.html");
                bufferedOutputStream.write(PropertiesUtil.get("error_response_head").getBytes());
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