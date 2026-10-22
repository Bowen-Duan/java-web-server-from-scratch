package com.briup.level04;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) {
        //声明服务器对象
        ServerSocket serverSocket = null;
        //声明线程池对象
        ExecutorService executorService = null;
        try {
            //创建服务器对象
            serverSocket = new ServerSocket(9999);

            //创建线程池对象
            executorService = Executors.newFixedThreadPool(10);

            System.out.println("服务器创建成功，等待客户端连接.....");

            //死循环
            while (true){
                //接收客户端连接
                Socket socket = serverSocket.accept();

                System.out.println("客户端连接成功，客户端IP："+socket.getLocalAddress());

                //创建任务
                Runnable task = new Task(socket);

                //调用线程池对象，并且分发任务
                executorService.submit(task);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            if (executorService != null){
                executorService.shutdown();
            }
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