package com.briup.level08.task;

import com.briup.level08.request.Request;
import com.briup.level08.request.impl.RequestImpl;
import com.briup.level08.response.Response;
import com.briup.level08.response.impl.ResponseImpl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

public class Task implements Runnable{

    private Socket socket;

    public Task(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        //声明请求对象
        Request request = null;
        //声明响应对象
        Response response = null;
        try {
            //创建请求对象  完成请求解析
            request = new RequestImpl(socket);

            //创建响应对象  完成响应构建以及发送
            response = new ResponseImpl(socket,request);
            //返回响应
            response.responseHandler();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (response.getBufferedOutputStream() != null){
                    response.getBufferedOutputStream().close();
                }
                if (response.getBufferedInputStream() != null){
                    response.getBufferedInputStream().close();
                }
                if(request.getBufferedReader() != null){
                    request.getBufferedReader().close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}