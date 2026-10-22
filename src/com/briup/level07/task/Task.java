package com.briup.level07.task;

import com.briup.level07.request.Request;
import com.briup.level07.request.impl.RequestImpl;
import com.briup.level07.response.Response;
import com.briup.level07.response.impl.ResponseImpl;

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
        //声明请求对象
        Request request = null;

        //声明响应对象
        Response response = null;

        try {
            //创建请求对象
            request = new RequestImpl(socket);

            //创建响应对象
            response = new ResponseImpl(socket,request.getPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                if (response.getBufferedOutputStream() != null){
                    response.getBufferedOutputStream().close();
                }

                if (response.getBufferedInputStream() != null){
                    response.getBufferedInputStream().close();
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